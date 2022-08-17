

<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">Winter v1.0.0</h1>
<h4 align="center">基于SpringBoot+Vue前后端分离的Java快速开发框架(基于RuoYi v.3.8.3)</h4>



## 平台简介

Winter是基于若依框架进行改造的。

* 前端采用Vue、Element UI。
* 后端采用Spring Boot、Spring Security、Redis & Jwt。
* 权限认证使用Jwt，支持多终端认证系统。
* 支持加载动态权限菜单，多方式轻松权限控制。
* 高效率开发，使用代码生成器可以一键生成前后端代码。&nbsp;

## 内置功能

1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.  部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3.  岗位管理：配置系统用户所属担任职务。
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.  字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7.  参数管理：对系统动态配置常用参数。
8.  通知公告：系统通知公告信息发布维护。
9.  操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
10. 登录日志：系统登录日志记录查询包含登录异常。
11. 在线用户：当前系统中活跃用户状态监控。
12. 定时任务：在线（添加、修改、删除)任务调度包含执行结果日志。
13. 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 。
14. 系统接口：根据业务代码自动生成相关的api接口文档。
15. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
16. 缓存监控：对系统的缓存信息查询，命令统计等。
17. 在线构建器：拖动表单元素生成相应的HTML代码。
18. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。

## Winter新增功能

1.依赖包

(1)MyBatis-plus

(2)mybatis-plus-join

(3)Knife4j

(4)Lombok

2.工具类

(1)AutoMapUtils:bean,List的属性拷贝工具类.

(2)GenericUtils:泛型工具类,获取泛型的真实类型.

(3)tuple工具包:1个到5个的临时存储变量工具类.

3.枚举类

(1)BaseEnum:基础枚举接口,结合EnumUtil可根据code以及枚举类型获取对应的枚举对象,枚举msg.具体模块可创建枚举类实现.

(2)BaseResultEnum:基础的响应枚举接口,结合BusinessException可抛出异常.具体模块可创建枚举类实现.

4.实体

(1)PrimaryKey:主键,自定义类型,实现该接口的实体需要定义字段id,设计的表需要有id.

(2)Auditing:审计抽象.

(3)CreateAuditing:创建审计,实现该接口的实体需要定义字段gmtCreate,createdUserId,设计的表需要有gmt_create,created_user_id.

(4)ModifiedAuditing:修改审计,实现该接口的实体需要定义字段gmtModified,modifiedUserId,设计的表需要有gmt_modified,modified_user_id.

(4)DeleteAuditing:删除审计,实现该接口的实体需要定义字段gmtDelete,deletedUserId,设计的表需要有gmt_delete,deleted_user_id.

(5)Entity:继承PrimaryKey,DeleteAuditing

(6)DefaultBaseEntity:默认实体,实现主键,创建审计,修改审计,删除审计.

5.Mapper

(1)DefaultBaseMapper:默认的基础Mapper,继承MyBatis-plus的BaseMapper以及MyBatis-plus-join的MPJBaseMapper.提供批量删除实体时自动填充删除审计字段的值.新建的Mapper可直接继承该接口.

6.Service

(1)IBaseService:基础业务接口,实现类BaseService.定义常用的增删改查方法,新增的业务可直接继承该业务接口,提供了addBefore,addAfter等方法给子类重写.

7.Controller

(1)DefaultController:默认的Controller,继承了若依的BaseController.定义了常用的增删改查接口.

8.Dto

(1)IdInput:id入参

(2)IdsInput:id列表入参

(3)PageQuery:基础分页入参

9.Config

(1)MyBatis-plus配置:分页,SQL注入(自动填充,连表查询)