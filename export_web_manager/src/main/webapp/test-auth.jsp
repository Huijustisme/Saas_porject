<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>测试Shiro的JSP权限标签</title>
</head>
<body>

<shiro:hasPermission name="企业管理">
<button>企业管理</button>
</shiro:hasPermission>
<hr/>
<shiro:hasPermission name="用户管理">
<button>用户管理</button>
</shiro:hasPermission>

</body>
</html>
