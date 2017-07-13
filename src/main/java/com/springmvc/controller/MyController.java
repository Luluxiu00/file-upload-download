package com.springmvc.controller;

import com.springmvc.pojo.Person;
import com.springmvc.service.PersonService;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 326944 on 2017/7/13.
 */
@Controller
public class MyController {

    private String filename;
    private String path;

    /**
     * 单个文件上传
     * @param uploadFile
     * @param session
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/file/upload",method = RequestMethod.POST)
    public String fileUpload(MultipartFile uploadFile, HttpSession session) throws IOException {
        //判断是否选择了文件
        if (uploadFile.getSize() == 0){
            return "error";
        }
        //获取文件名
        filename = uploadFile.getOriginalFilename();
        if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".gif")){
            //前半部分路径
            path = session.getServletContext().getRealPath("/images");
            //进行路径拼接=前半部分路径+文件名
            File file = new File(path,filename);
            CommonsMultipartResolver xx;
            uploadFile.transferTo(file);
            return "success";
        }else{
            return "error";
        }
    }

    /**
     * 多文件上传
     * @param uploadFile
     * @param session
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/file/uploadAll",method = RequestMethod.POST)
    public String fileUpload(MultipartFile[] uploadFile, HttpSession session) throws IOException {
        for (MultipartFile item : uploadFile) {
            if (item.getSize() > 0) {
                //获取文件名
                filename = item.getOriginalFilename();
                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".gif")) {
                    //前半部分路径
                    path = session.getServletContext().getRealPath("/images");
                    //进行路径拼接=前半部分路径+文件名
                    File file = new File(path, filename);
                    item.transferTo(file);
                }
            }else{
                    return "error";
                }
        }
        return "success";
    }


    @RequestMapping("/file/download")
    public ResponseEntity<byte[]> fileDownload() throws IOException {
        File file = new File("D:\\326944\\Downloads\\fileupdown\\src\\main\\webapp\\images\\3.jpg");
        HttpHeaders headers = new HttpHeaders();
        filename = new String("哈哈.jpg".getBytes("UTF-8"),"ISO-8859-1");
        headers.setContentDispositionFormData("attachment",filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.CREATED);
    }





    /**
     * 读取Excel
     * @param uploadFile
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/file/upload/excel",method = RequestMethod.POST)
    public String excelUpload(MultipartFile uploadFile) throws IOException {
        if (uploadFile.getSize() > 0){
            //创建对Excel工作簿文件的引用
            FileInputStream fis = new FileInputStream("E:\\person.xlsx");
            Workbook book = null;
            try {
                book = new XSSFWorkbook(fis);
            } catch (Exception ex) {
                book = new HSSFWorkbook(fis);
            }
            //创建对工作表的引用
            Sheet sheet = book.getSheetAt(0);
            List<Person> list = new ArrayList<Person>();
            //解析除了sheet第一行之外的数据,封装到List中
            for(Row row : sheet){
                //第一行除外
                int rowNum = row.getRowNum();
                if (rowNum == 0) continue;

                //一行
                Person person = new Person();
                person.setId(row.getCell(0).getNumericCellValue() + "");
                person.setName(row.getCell(1).getStringCellValue());
                person.setAge(row.getCell(2).getNumericCellValue() + "");
                person.setAddress(row.getCell(3).getStringCellValue());
                list.add(person);
            }

            System.out.println(list);
        }
            return "success";
    }



    @Autowired
    PersonService personService;

    @RequestMapping("/file/download/excel")
    public void excelDownload(HttpServletResponse response) throws IOException {
        List<Person> list = personService.findAll();
        System.out.println(list);

        //创建工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建sheet页
        HSSFSheet sheet = workbook.createSheet();
        //表头
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("id");
        row.createCell(1).setCellValue("name");
        row.createCell(2).setCellValue("age");
        row.createCell(3).setCellValue("address");

        int index = 1;
        for(Person p : list){
            HSSFRow row1 = sheet.createRow(index++);
            row1.createCell(0).setCellValue(p.getId());
            row1.createCell(1).setCellValue(p.getName());
            row1.createCell(2).setCellValue(p.getAge());
            row1.createCell(3).setCellValue(p.getAddress());
        }

        response.setContentType("application/octet-stream;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        String filename = "哈哈.xlsx";
        response.addHeader("Content-Disposition", "attachment; filename="+new String(filename.getBytes("gbk"),"iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.close();

    }
}
