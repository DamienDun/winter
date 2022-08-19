package com.winter.file.storage.clients.huawei;

import com.obs.services.ObsClient;
import com.obs.services.model.*;
import com.winter.common.utils.ExceptionUtil;
import com.winter.common.utils.StringUtils;
import com.winter.common.utils.tuple.TupleTwo;
import com.winter.file.storage.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.DisposableBean;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 华为云 OBS
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/17 14:20
 */
public class HuaWeiStorageClient extends AbstractCloudStorageClient<HuaWeiBucket> implements Closeable, DisposableBean {

    /**
     * 存储代码
     */
    public static final String CHANNEL_ID = "HuaWeiOBS";

    /**
     * 存储名称
     */
    public static final String CHANNEL_NAME = "华为云对象存储";

    /**
     * Obs客户端
     */
    protected final ObsClient obsClient;

    /**
     * HuaWeiStorageClient
     *
     * @param endpoint
     * @param defaultBucketName
     * @param accessKey
     * @param secretKey
     */
    public HuaWeiStorageClient(String endpoint, String defaultBucketName,
                               String accessKey, String secretKey) {
        super(endpoint, defaultBucketName, accessKey, secretKey);
        this.obsClient = new ObsClient(this.getAccessKey(), this.getSecretKey(), this.getEndpoint());
    }

    /**
     * MinioStorageClient
     *
     * @param properties 属性
     */
    public HuaWeiStorageClient(HuaWeiStorageClientProperties properties) {
        this(properties.getEndpoint(),
                properties.getDefaultBucketName(),
                properties.getAccessKey(),
                properties.getSecretKey());
        this.setStorageClientProperties(properties);
    }

    @Override
    public boolean existBucket(String bucketName) {
        ExceptionUtil.checkNotNullOrBlank(bucketName, "bucketName");
        return this.obsClient.headBucket(bucketName);
    }

    @Override
    public HuaWeiBucket createBucket(String bucketName) {
        ExceptionUtil.checkNotNullOrBlank(bucketName, "bucketName");
        ObsBucket obsBucket = this.obsClient.createBucket(bucketName);
        this.obsClient.setBucketAcl(bucketName, AccessControlList.REST_CANNED_PUBLIC_READ);
        HuaWeiBucket bucket = new HuaWeiBucket(bucketName);
        bucket.setCreationDate(obsBucket.getCreationDate());
        bucket.setLocation(obsBucket.getLocation());
        return bucket;
    }

    @Override
    public HuaWeiBucket getBucket(String bucketName) {
        ExceptionUtil.checkNotNullOrBlank(bucketName, "bucketName");
        if (this.obsClient.headBucket(bucketName)) {
            BucketMetadataInfoResult result = this.obsClient.getBucketMetadata(new BucketMetadataInfoRequest(bucketName));
            HuaWeiBucket bucket = new HuaWeiBucket(bucketName);
            //bucket.setCreationDate(result.getCreationDate());
            bucket.setLocation(result.getLocation());
        }
        return null;
    }

    /**
     * 设置文件选项
     *
     * @param options 选项
     * @param request 请求
     */
    protected void setHeaders(ObjectMetadata options, FileStorageRequest request) {
        String contentType = this.getFileContentType(request.getFileInfo().getExtensionName());
        if (StringUtils.isNotEmpty(contentType)) {
            options.setContentType(contentType);
        }
        String contentDisposition = this.getContentDisposition(request);
        if (StringUtils.isNotEmpty(contentDisposition)) {
            options.getMetadata().put(CONTENT_DISPOSITION, contentDisposition);
        }
    }

    @Override
    public FileObject saveFile(FileStorageRequest request) throws Exception {
        ExceptionUtil.checkNotNull(request, "request");
        String bucketName = this.checkBucketName(request.getBucketName());
        if (!this.obsClient.headBucket(bucketName)) {
            this.obsClient.createBucket(bucketName);
            this.obsClient.setBucketAcl(bucketName, AccessControlList.REST_CANNED_PUBLIC_READ);
        }
        InputStream inputStream = null;
        try {
            TupleTwo<Long, InputStream> two = this.readRequestStream(request);
            long size = two.getItem1();
            inputStream = two.getItem2();
            ObjectMetadata options = new ObjectMetadata();
            options.setContentLength(size);
            this.setHeaders(options, request);
            PutObjectResult objectResult = this.obsClient.putObject(bucketName, request.getFileInfo().getFullPath(), inputStream, options);
            FileObject fileObject = new FileObject();
            fileObject.setFileInfo(request.getFileInfo());
            fileObject.getFileInfo().setLength(size);
            fileObject.setUrl(request.getFileInfo().getFullPath());
            fileObject.setAccessUrl(objectResult.getObjectUrl());
            return fileObject;
        } finally {
            IOUtils.closeQuietly(request.getInputStream());
            IOUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public FileStorageObject getFile(String bucketName, String fullPath) {
        bucketName = this.checkBucketName(bucketName);
        ExceptionUtil.checkNotNullOrBlank(fullPath, "fullPath");
        FileInfo fileInfo = new FileInfo(fullPath, true);
        ObsObject obsObject = this.obsClient.getObject(bucketName, fileInfo.getFullPath());
        if (obsObject == null) {
            return null;
        }
        FileStorageObject fileStorageObject = new FileStorageObject();
        fileStorageObject.setFileInfo(new FileInfo(fullPath, true));
        fileStorageObject.setAccessUrl(this.getAccessUrl(bucketName, fileInfo));
        fileStorageObject.getFileInfo().setLength(obsObject.getMetadata().getContentLength());
        fileStorageObject.setUrl(fileInfo.getFullPath());
        fileStorageObject.setInputStream(obsObject.getObjectContent());
        return fileStorageObject;
    }

    @Override
    public boolean existFile(String bucketName, String fullPath) {
        bucketName = this.checkBucketName(bucketName);
        ExceptionUtil.checkNotNullOrBlank(fullPath, "fullPath");
        FileInfo fileInfo = new FileInfo(fullPath, true);
        return this.obsClient.doesObjectExist(bucketName, fileInfo.getFullPath());
    }

    @Override
    public void deleteFile(String bucketName, String fullPath) {
        bucketName = this.checkBucketName(bucketName);
        ExceptionUtil.checkNotNullOrBlank(fullPath, "fullPath");
        FileInfo fileInfo = new FileInfo(fullPath, true);
        this.obsClient.deleteObject(bucketName, fileInfo.getFullPath());
    }

    @Override
    public List<FileObject> listFileObjects(String bucketName, String prefix) {
        bucketName = this.checkBucketName(bucketName);
        ListObjectsRequest objectsRequest = new ListObjectsRequest(bucketName);
        objectsRequest.setPrefix(prefix);
        ObjectListing listing = this.obsClient.listObjects(objectsRequest);
        if (listing.getObjects() == null) {
            return new ArrayList<>();
        }
        List<FileObject> fileObjects = new ArrayList<>(listing.getObjects().size());
        for (ObsObject object : listing.getObjects()) {
            FileObject fileObject = new FileObject();
            FileInfo fileInfo = new FileInfo(object.getObjectKey(),
                    object.getMetadata().getContentLength() != null
                            && object.getMetadata().getContentLength() > 0);
            fileInfo.setLength(object.getMetadata().getContentLength());
            fileObject.setFileInfo(fileInfo);
            fileObject.setUrl(object.getObjectKey());
            fileObject.setAccessUrl(this.getAccessUrl(bucketName, object.getObjectKey()));
            fileObjects.add(fileObject);
        }
        return fileObjects;
    }

    @Override
    public String getChannelId() {
        return CHANNEL_ID;
    }

    @Override
    public String getChannelName() {
        return CHANNEL_NAME;
    }

    @Override
    public String toString() {
        return "启用 FileStorage " + this.getChannelName()
                + " 默认分区:" + this.getDefaultBucketName()
                + " 访问根路径:" + this.getEndpoint();
    }

    @Override
    public void destroy() throws Exception {
        this.close();
    }

    @Override
    public void close() throws IOException {
        this.obsClient.close();
    }
}
