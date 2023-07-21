package com.winter.framework.web.exception;

import com.winter.common.constant.Constants;
import com.winter.common.constant.HttpStatus;
import com.winter.common.core.domain.AjaxResult;
import com.winter.common.exception.BusinessException;
import com.winter.common.exception.DemoModeException;
import com.winter.common.exception.ServiceException;
import com.winter.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 全局异常处理器
 *
 * @author winter
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 权限校验异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public AjaxResult handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
        return AjaxResult.error(HttpStatus.FORBIDDEN, "没有权限，请联系管理员授权");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                          HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        response.addHeader(Constants.RESP_ACCESS_CONTROL_EXPOSE_HEADERS, Constants.RESP_HEADER_CODE);
        response.addHeader(Constants.RESP_HEADER_CODE, String.valueOf(HttpStatus.ERROR));
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request, HttpServletResponse response) {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        response.addHeader(Constants.RESP_ACCESS_CONTROL_EXPOSE_HEADERS, Constants.RESP_HEADER_CODE);
        response.addHeader(Constants.RESP_HEADER_CODE, String.valueOf(code));
        return StringUtils.isNotNull(code) ? AjaxResult.error(code, e.getMessage()) : AjaxResult.error(e.getMessage());
    }

    /**
     * 请求路径中缺少必需的路径变量
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public AjaxResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求路径中缺少必需的路径变量'{}',发生系统异常.", requestURI, e);
        return AjaxResult.error(String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName()));
    }

    /**
     * 请求参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public AjaxResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求参数类型不匹配'{}',发生系统异常.", requestURI, e);
        Class<?> requiredType = e.getRequiredType();
        String requiredTypeName = Objects.nonNull(requiredType) ? requiredType.getName() : "";
        return AjaxResult.error(String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'", e.getName(), requiredTypeName, e.getValue()));
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        response.addHeader(Constants.RESP_ACCESS_CONTROL_EXPOSE_HEADERS, Constants.RESP_HEADER_CODE);
        response.addHeader(Constants.RESP_HEADER_CODE, String.valueOf(HttpStatus.ERROR));
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e, HttpServletResponse response) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        response.addHeader(Constants.RESP_ACCESS_CONTROL_EXPOSE_HEADERS, Constants.RESP_HEADER_CODE);
        response.addHeader(Constants.RESP_HEADER_CODE, String.valueOf(HttpStatus.ERROR));
        return AjaxResult.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletResponse response) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        response.addHeader(Constants.RESP_ACCESS_CONTROL_EXPOSE_HEADERS, Constants.RESP_HEADER_CODE);
        response.addHeader(Constants.RESP_HEADER_CODE, String.valueOf(HttpStatus.ERROR));
        return AjaxResult.error(message);
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public AjaxResult handleDemoModeException(DemoModeException e, HttpServletResponse response) {
        response.addHeader(Constants.RESP_ACCESS_CONTROL_EXPOSE_HEADERS, Constants.RESP_HEADER_CODE);
        response.addHeader(Constants.RESP_HEADER_CODE, String.valueOf(HttpStatus.ERROR));
        return AjaxResult.error("演示模式，不允许操作");
    }

    /**
     * 业务模块异常
     */
    @ExceptionHandler(BusinessException.class)
    public AjaxResult handleBindException(BusinessException e, HttpServletResponse response) {
        log.error(e.getMessage(), e);
        response.addHeader(Constants.RESP_ACCESS_CONTROL_EXPOSE_HEADERS, Constants.RESP_HEADER_CODE);
        response.addHeader(Constants.RESP_HEADER_CODE, String.valueOf(e.getResultEnum().getCode()));
        return AjaxResult.error(e.getResultEnum().getCode(), e.getMessage());
    }
}
