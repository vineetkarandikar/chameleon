package com.digital.chameleon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Test {

  public static void main(String args[]) throws IOException {
    // File file = new File("C:/Users/Lenovo/Downloads/OpTransactionHistoryUX309-03-2022.pdf");
    // PDDocument document = PDDocument.load(file);
    // PDFTextStripper pdfStripper = new PDFTextStripper();
    // String text = pdfStripper.getText(document);
    // System.out.println(text);
    // document.close();
    String fileName = "C:/Users/Lenovo/Downloads/Vineet Karandikar_resume.docx";
    XWPFDocument doc = new XWPFDocument(Files.newInputStream(Paths.get(fileName)));
    XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(doc);
    String docText = xwpfWordExtractor.getText();
    System.out.println(docText);
    // find number of words in the document
    long count = Arrays.stream(docText.split("\\s+")).count();
    System.out.println("Total words: " + count);
  }

}
