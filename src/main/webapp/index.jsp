<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/file/upload/excel.do" method="post" enctype="multipart/form-data">
        <h2>单个文件上传(Excel)</h2>
        文件: <input type="file" name="uploadFile"><br/><br/>
        <input type="submit" value="上传">
    </form>
    <br/><br/><br/>

    <form action="${pageContext.request.contextPath}/file/uploadAll.do" method="post" enctype="multipart/form-data">
        <h2>多个文件上传</h2>
        文件1: <input type="file" name="uploadFile"><br/><br/>
        文件2: <input type="file" name="uploadFile"><br/><br/>
        文件3: <input type="file" name="uploadFile"><br/><br/>
        <input type="submit" value="上传">
    </form>

    <br/><br/>
    <h2>文件下载(图片)</h2>
    <a href="${pageContext.request.contextPath }/file/download.do">下载</a>

    <br/><br/>
    <h2>文件下载(Excel)</h2>
    <a href="${pageContext.request.contextPath }/file/download/excel.do">下载</a>
</body>
</html>
