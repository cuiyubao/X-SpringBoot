package com.suke.czx.modules.apk.service;


import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.utils.PdfMerger;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


/**
 * @author cuiyubao
 * @date 2024/9/29 20:58
 */
@Service
@Slf4j
public class TestService {


    public void dealPdf(){
        String inputFilePath = "/Users/cuiyubao/Downloads/aaa.pdf"; // 输入PDF文件路径
//        String inputFilePath = "/Users/cuiyubao/Downloads/鲁晓春_报价单.pdf"; // 输入PDF文件路径
        String outputFilePath = "/Users/cuiyubao/aa.pdf"; // 输出PDF文件路径
        String fontFilePath = "/Users/cuiyubao/Downloads/Noto_Sans_SC/static/NotoSansSC-Light.ttf"; // 中文字体文件路径

        try (PDDocument document = PDDocument.load(new File(inputFilePath))) {
            // 获取最后一页
            PDPage lastPage = document.getPage(document.getNumberOfPages() - 1);

            // 获取页面尺寸
            PDRectangle mediaBox = lastPage.getMediaBox();
            float pageWidth = mediaBox.getWidth();
            float pageHeight = mediaBox.getHeight();
            // 打印页面信息
            System.out.println("Page Width: " + pageWidth);
            System.out.println("Page Height: " + pageHeight);
            System.out.println("Page Rotation: " + lastPage.getRotation());
            System.out.println("CropBox: " + lastPage.getCropBox());

            float cropX = lastPage.getCropBox().getLowerLeftX();
            float cropY = lastPage.getCropBox().getLowerLeftY();
            float cropWidth = lastPage.getCropBox().getWidth();
            float cropHeight = lastPage.getCropBox().getHeight();
            System.out.println("CropBox X: " + cropX + ", Y: " + cropY);
            System.out.println("CropBox Width: " + cropWidth + ", Height: " + cropHeight);
            System.out.println("Page Matrix: " + lastPage.getMatrix());


            float fontSize = 500; // 设置字体大小

            // 加载字体
            PDType0Font font = PDType0Font.load(document, new File(fontFilePath));

            // 计算文字的宽度
            String text = "甲方(签名/盖章)";

            // 计算目标坐标（页面中下方）
            float x = pageWidth - 100; // 根据需要调整
            float y = pageHeight - 20; // 根据需要调整
            // 开始向页面添加内容
            try (PDPageContentStream contentStream = new PDPageContentStream(document, lastPage, PDPageContentStream.AppendMode.APPEND, true)) {
                contentStream.setFont(font, fontSize); // 设置字体和大小
                contentStream.beginText();
                contentStream.newLineAtOffset(x*10, y);
                contentStream.showText("甲方(签名/盖章)"); // 添加中文文本
                contentStream.endText();
            }
            // 保存修改后的PDF
            document.save(outputFilePath);
            System.out.println("中文文本已成功添加到PDF文件的右下角！");
        } catch (IOException e) {
            System.err.println("处理PDF文件时出错: " + e.getMessage());
        }
    }


    public void dealPdf1() {
        String inputFilePath = "https://file.ljcdn.com/utopia-file/fb3321d46094cc8633c7c62d1c28f589.pdf"; // 输入 PDF 文件路径
//        String inputFilePath = "/Users/cuiyubao/Desktop/个性化签约告知函.pdf"; // 输入 PDF 文件路径
        String outputFilePath = "/Users/cuiyubao/aaa.pdf"; // 输出 PDF 文件路径
        String textToAdd = "这是中文文字"; // 要添加的文字

        try {
            // 加载支持中文的字体文件
            String fontPath = "/Users/cuiyubao/Downloads/Noto_Sans_SC/static/NotoSansSC-Light.ttf"; // macOS 示例路径
            PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);

            // 读取已有 PDF
            PdfReader reader = new PdfReader(inputFilePath);
            PdfWriter writer = new PdfWriter(outputFilePath);
            PdfDocument pdfDoc = new PdfDocument(reader, writer);

            // 获取总页数
            int totalPages = pdfDoc.getNumberOfPages();

            // 遍历每一页，添加文字
            for (int i = 1; i <= totalPages; i++) {
                System.out.println("正在处理第 " + i + " 页...");
                PdfPage page = pdfDoc.getPage(i);
                PdfCanvas canvas = new PdfCanvas(page);

                // 获取页面尺寸
                Rectangle pageSize = page.getPageSize();
                float x = pageSize.getRight()/2; // 假设右下角距离右边 150
                float y = pageSize.getBottom() + 100; // 假设右下角距离底部 50

                // 设置文字属性并添加文字
                canvas.beginText();
                canvas.setFontAndSize(font, 25); // 使用中文字体并设置大小
                canvas.moveText(x, y); // 移动到指定坐标
                canvas.showText(textToAdd); // 显示文字
                canvas.endText();
            }

            // 保存并关闭
            pdfDoc.close();
            System.out.println("文字已成功添加到 PDF: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void splicePdf() {

        List<String> pathList = Arrays.asList(
                "https://file.ljcdn.com/utopia-file/16ae51acf52303495de26107ee277dc5.pdf",
                "https://file.ljcdn.com/utopia-file/fb3321d46094cc8633c7c62d1c28f589.pdf",
                "https://file.ljcdn.com/utopia-file/386172340539e28252bd37d77c5c5941.pdf",
                "https://file.ljcdn.com/utopia-file/badeef6fb8885ec8419c20bea798e43f.pdf",
                "https://file.ljcdn.com/utopia-file/29d361c2f1e6af90aec0f022b6da97c3.pdf",
                "https://file.ljcdn.com/utopia-file/abbf431a03ac16c91bb76b73bb84e198.pdf",
                "https://file.ljcdn.com/utopia-file/5a7289770bce186140416a1e41525a73.pdf",
                "https://file.ljcdn.com/utopia-file/49b772acea3483a2e6ceac43faa8e161.pdf",
                "https://file.ljcdn.com/utopia-file/86c8fb2500962764d2859a3e4756f70a.pdf",
                "https://file.ljcdn.com/utopia-file/7e849ec26d4e948cc3d595b23ee0b8c8.pdf",
                "https://file.ljcdn.com/utopia-file/7776c026e4b3e0b944af57d8d24ae555.pdf",
                "https://file.ljcdn.com/utopia-file/e5e30c4b9c9a340c64965cecd43c971f.pdf",
                "https://file.ljcdn.com/utopia-file/f3995cb1dda9fda82ca445d09bcdc67b.pdf",
                "https://file.ljcdn.com/utopia-file/03dd6f310ff5b519f08c3178a30c5fae.pdf",
                "https://file.ljcdn.com/utopia-file/fc3e2d931c23786e2735f054f6046d71.pdf",
                "https://file.ljcdn.com/utopia-file/50b8f2935166263a1458d0e213b59591.pdf",
                "https://file.ljcdn.com/utopia-file/5d99031ed2f87da71e8d3f8726491716.pdf",
                "https://file.ljcdn.com/utopia-file/fb27b0845ed515b54b8b5fee7d412e5e.pdf",
                "https://file.ljcdn.com/utopia-file/c32ab63bd6b482653ff86ee8111aaf1e.pdf",
                "https://file.ljcdn.com/utopia-file/16ae51acf52303495de26107ee277dc5.pdf",
                "https://file.ljcdn.com/utopia-file/fb3321d46094cc8633c7c62d1c28f589.pdf",
                "https://file.ljcdn.com/utopia-file/386172340539e28252bd37d77c5c5941.pdf",
                "https://file.ljcdn.com/utopia-file/badeef6fb8885ec8419c20bea798e43f.pdf",
                "https://file.ljcdn.com/utopia-file/29d361c2f1e6af90aec0f022b6da97c3.pdf",
                "https://file.ljcdn.com/utopia-file/abbf431a03ac16c91bb76b73bb84e198.pdf",
                "https://file.ljcdn.com/utopia-file/5a7289770bce186140416a1e41525a73.pdf",
                "https://file.ljcdn.com/utopia-file/49b772acea3483a2e6ceac43faa8e161.pdf",
                "https://file.ljcdn.com/utopia-file/86c8fb2500962764d2859a3e4756f70a.pdf",
                "https://file.ljcdn.com/utopia-file/7e849ec26d4e948cc3d595b23ee0b8c8.pdf",
                "https://file.ljcdn.com/utopia-file/7776c026e4b3e0b944af57d8d24ae555.pdf",
                "https://file.ljcdn.com/utopia-file/e5e30c4b9c9a340c64965cecd43c971f.pdf",
                "https://file.ljcdn.com/utopia-file/f3995cb1dda9fda82ca445d09bcdc67b.pdf",
                "https://file.ljcdn.com/utopia-file/03dd6f310ff5b519f08c3178a30c5fae.pdf",
                "https://file.ljcdn.com/utopia-file/fc3e2d931c23786e2735f054f6046d71.pdf",
                "https://file.ljcdn.com/utopia-file/50b8f2935166263a1458d0e213b59591.pdf",
                "https://file.ljcdn.com/utopia-file/5d99031ed2f87da71e8d3f8726491716.pdf",
                "https://file.ljcdn.com/utopia-file/fb27b0845ed515b54b8b5fee7d412e5e.pdf",
                "https://file.ljcdn.com/utopia-file/c32ab63bd6b482653ff86ee8111aaf1e.pdf",
                "https://file.ljcdn.com/utopia-file/16ae51acf52303495de26107ee277dc5.pdf",
                "https://file.ljcdn.com/utopia-file/fb3321d46094cc8633c7c62d1c28f589.pdf",
                "https://file.ljcdn.com/utopia-file/386172340539e28252bd37d77c5c5941.pdf",
                "https://file.ljcdn.com/utopia-file/badeef6fb8885ec8419c20bea798e43f.pdf",
                "https://file.ljcdn.com/utopia-file/29d361c2f1e6af90aec0f022b6da97c3.pdf",
                "https://file.ljcdn.com/utopia-file/abbf431a03ac16c91bb76b73bb84e198.pdf",
                "https://file.ljcdn.com/utopia-file/5a7289770bce186140416a1e41525a73.pdf",
                "https://file.ljcdn.com/utopia-file/49b772acea3483a2e6ceac43faa8e161.pdf",
                "https://file.ljcdn.com/utopia-file/86c8fb2500962764d2859a3e4756f70a.pdf",
                "https://file.ljcdn.com/utopia-file/7e849ec26d4e948cc3d595b23ee0b8c8.pdf",
                "https://file.ljcdn.com/utopia-file/7776c026e4b3e0b944af57d8d24ae555.pdf",
                "https://file.ljcdn.com/utopia-file/e5e30c4b9c9a340c64965cecd43c971f.pdf",
                "https://file.ljcdn.com/utopia-file/f3995cb1dda9fda82ca445d09bcdc67b.pdf",
                "https://file.ljcdn.com/utopia-file/03dd6f310ff5b519f08c3178a30c5fae.pdf",
                "https://file.ljcdn.com/utopia-file/fc3e2d931c23786e2735f054f6046d71.pdf",
                "https://file.ljcdn.com/utopia-file/50b8f2935166263a1458d0e213b59591.pdf",
                "https://file.ljcdn.com/utopia-file/5d99031ed2f87da71e8d3f8726491716.pdf",
                "https://file.ljcdn.com/utopia-file/fb27b0845ed515b54b8b5fee7d412e5e.pdf",
                "https://file.ljcdn.com/utopia-file/c32ab63bd6b482653ff86ee8111aaf1e.pdf",
                "https://file.ljcdn.com/utopia-file/16ae51acf52303495de26107ee277dc5.pdf",
                "https://file.ljcdn.com/utopia-file/fb3321d46094cc8633c7c62d1c28f589.pdf",
                "https://file.ljcdn.com/utopia-file/386172340539e28252bd37d77c5c5941.pdf",
                "https://file.ljcdn.com/utopia-file/badeef6fb8885ec8419c20bea798e43f.pdf",
                "https://file.ljcdn.com/utopia-file/29d361c2f1e6af90aec0f022b6da97c3.pdf",
                "https://file.ljcdn.com/utopia-file/abbf431a03ac16c91bb76b73bb84e198.pdf",
                "https://file.ljcdn.com/utopia-file/5a7289770bce186140416a1e41525a73.pdf",
                "https://file.ljcdn.com/utopia-file/49b772acea3483a2e6ceac43faa8e161.pdf",
                "https://file.ljcdn.com/utopia-file/86c8fb2500962764d2859a3e4756f70a.pdf",
                "https://file.ljcdn.com/utopia-file/7e849ec26d4e948cc3d595b23ee0b8c8.pdf",
                "https://file.ljcdn.com/utopia-file/7776c026e4b3e0b944af57d8d24ae555.pdf",
                "https://file.ljcdn.com/utopia-file/e5e30c4b9c9a340c64965cecd43c971f.pdf",
                "https://file.ljcdn.com/utopia-file/f3995cb1dda9fda82ca445d09bcdc67b.pdf",
                "https://file.ljcdn.com/utopia-file/03dd6f310ff5b519f08c3178a30c5fae.pdf",
                "https://file.ljcdn.com/utopia-file/fc3e2d931c23786e2735f054f6046d71.pdf",
                "https://file.ljcdn.com/utopia-file/50b8f2935166263a1458d0e213b59591.pdf",
                "https://file.ljcdn.com/utopia-file/5d99031ed2f87da71e8d3f8726491716.pdf",
                "https://file.ljcdn.com/utopia-file/fb27b0845ed515b54b8b5fee7d412e5e.pdf",
                "https://file.ljcdn.com/utopia-file/c32ab63bd6b482653ff86ee8111aaf1e.pdf",
                "https://file.ljcdn.com/utopia-file/16ae51acf52303495de26107ee277dc5.pdf",
                "https://file.ljcdn.com/utopia-file/fb3321d46094cc8633c7c62d1c28f589.pdf",
                "https://file.ljcdn.com/utopia-file/386172340539e28252bd37d77c5c5941.pdf",
                "https://file.ljcdn.com/utopia-file/badeef6fb8885ec8419c20bea798e43f.pdf",
                "https://file.ljcdn.com/utopia-file/29d361c2f1e6af90aec0f022b6da97c3.pdf",
                "https://file.ljcdn.com/utopia-file/abbf431a03ac16c91bb76b73bb84e198.pdf",
                "https://file.ljcdn.com/utopia-file/5a7289770bce186140416a1e41525a73.pdf",
                "https://file.ljcdn.com/utopia-file/49b772acea3483a2e6ceac43faa8e161.pdf",
                "https://file.ljcdn.com/utopia-file/86c8fb2500962764d2859a3e4756f70a.pdf",
                "https://file.ljcdn.com/utopia-file/7e849ec26d4e948cc3d595b23ee0b8c8.pdf",
                "https://file.ljcdn.com/utopia-file/7776c026e4b3e0b944af57d8d24ae555.pdf",
                "https://file.ljcdn.com/utopia-file/e5e30c4b9c9a340c64965cecd43c971f.pdf",
                "https://file.ljcdn.com/utopia-file/f3995cb1dda9fda82ca445d09bcdc67b.pdf",
                "https://file.ljcdn.com/utopia-file/03dd6f310ff5b519f08c3178a30c5fae.pdf",
                "https://file.ljcdn.com/utopia-file/fc3e2d931c23786e2735f054f6046d71.pdf",
                "https://file.ljcdn.com/utopia-file/50b8f2935166263a1458d0e213b59591.pdf",
                "https://file.ljcdn.com/utopia-file/5d99031ed2f87da71e8d3f8726491716.pdf",
                "https://file.ljcdn.com/utopia-file/fb27b0845ed515b54b8b5fee7d412e5e.pdf",
                "https://file.ljcdn.com/utopia-file/c32ab63bd6b482653ff86ee8111aaf1e.pdf"
        );


        String outputFilePath = "/Users/cuiyubao/merge.pdf"; // 输出合并后的文件路径
        try {
            // 创建输出 PDF 文档
            PdfDocument outputPdf = new PdfDocument(new PdfWriter(outputFilePath));
            PdfMerger merger = new PdfMerger(outputPdf);

            for (String url : pathList) {
                // 下载 PDF 到本地临时文件
                String tempFilePath = downloadPdf(url);

                // 加载 PDF 文件
                PdfDocument pdf = new PdfDocument(new PdfReader(tempFilePath));

                // 合并 PDF
                merger.merge(pdf, 1, pdf.getNumberOfPages());

                // 关闭当前 PDF
                pdf.close();
            }

            // 关闭输出 PDF
            outputPdf.close();
            System.out.println("PDF 文件已成功合并为: " + outputFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * 下载 PDF 文件到本地临时路径
     *
     * @param fileUrl PDF 文件的 URL
     * @return 本地临时文件路径
     */
    private static String downloadPdf(String fileUrl) throws Exception {
        String tempFilePath = "temp_" + System.currentTimeMillis() + ".pdf"; // 临时文件路径
        try (InputStream in = new BufferedInputStream(new URL(fileUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(tempFilePath)) {

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        }
        return tempFilePath;
    }

    public void sortPdf(){
        String url="/Users/cuiyubao/merge.pdf";
        try {
            compressPDF(url,"/Users/cuiyubao/merge_sort_30.pdf",40);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * 压缩 PDF 文件
     * @param inputPath 原始 PDF 文件路径
     * @param outputPath 压缩后的 PDF 文件路径
     * @param dpi 渲染的分辨率（数值越低，文件压缩越大）
     * @throws IOException 如果发生文件操作错误
     */
    public static void compressPDF(String inputPath, String outputPath, float dpi) throws IOException {
        try (PDDocument document = PDDocument.load(new File(inputPath))) {
            PDFRenderer renderer = new PDFRenderer(document);
            PDDocument compressedDoc = new PDDocument();

            // 遍历每一页，将其转换为压缩后的图像
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, dpi); // 使用指定 DPI 渲染页面
                PDPage page = new PDPage(new PDRectangle(image.getWidth(), image.getHeight()));
                compressedDoc.addPage(page);

                // 将渲染的图像嵌入新的 PDF
                PDImageXObject pdImage = PDImageXObject.createFromFileByContent(writeTempImage(image, i), compressedDoc);
                try (PDPageContentStream contentStream = new PDPageContentStream(compressedDoc, page)) {
                    contentStream.drawImage(pdImage, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());
                }
            }

            // 保存压缩后的 PDF
            compressedDoc.save(outputPath);
            compressedDoc.close();
        }
    }

    /**
     * 写入临时图像文件
     * @param image 缓存的图像
     * @param index 页码
     * @return 图像文件的路径
     * @throws IOException 如果发生写入错误
     */
    private static File writeTempImage(BufferedImage image, int index) throws IOException {
        File tempImageFile = new File("temp_page_" + index + ".jpg");
        ImageIO.write(image, "jpg", tempImageFile);
        return tempImageFile;
    }


}
