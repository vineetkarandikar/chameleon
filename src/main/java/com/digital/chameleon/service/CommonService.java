package com.digital.chameleon.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.digital.chameleon.common.Mail;
import com.digital.chameleon.security.entity.User;
import com.digital.chameleon.security.repo.UserRepository;
import com.google.zxing.WriterException;

/*
 * @Author Vineetkabacus@gmail.com
 * 
 * @Purpose This class is used for all common application operations.
 */
@Service
public class CommonService {

  private static final Logger logger = LoggerFactory.getLogger(CommonService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JavaMailSender javaMailSender;

  public Mail createMailBody(String userRole, User user, String toUserId) {
    Mail mail = new Mail();
    mail.setFrom(user.getEmailId());
    Optional<User> userTo = userRepository.findById(toUserId);
    mail.setMailTo(userTo.get().getEmailId());
    Map<String, Object> model = new HashMap<String, Object>();
    model.put("applicant",
        userTo.get().getEmailId().replaceAll("@.*", "").replaceAll("[^a-zA-Z]+", " ").trim());
    model.put("role", userRole);
    Date date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
    String strDate = dateFormat.format(date);
    model.put("application_date", strDate);
    model.put("emailId", mail.getMailTo());
    model.put("phoneNo", userTo.get().getMobile());
    mail.setProps(model);
    return mail;
  }

  public void sendOTP(User user, Integer OTP)
      throws WriterException, IOException, MessagingException {
    MimeMessage msg = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(msg, true);
    helper.setTo(user.getEmailId());
    helper.setSubject("OTP from Chameleon Job Portal for Login");
    StringBuilder emailBody = new StringBuilder();
    emailBody.append("<h1>");
    emailBody.append(OTP);
    helper.setText(emailBody.toString(), true);
    javaMailSender.send(msg);
  }

  public String convertBase64EncodedStringToPDFFileAndExtractText(String docFileBase64BitEncoded,
      String fileName) throws IOException {
    byte[] docAsBytes = Base64.getDecoder().decode(docFileBase64BitEncoded);
    Path path = Files.createTempFile(fileName, ".pdf");
    Files.write(path, docAsBytes);
    File file = new File(path.toString());
    PDDocument document = PDDocument.load(file);
    PDFTextStripper pdfStripper = new PDFTextStripper();
    String text = pdfStripper.getText(document);
    logger.info("Extracted test from document formed from encoded base64 string " + text);
    document.close();
    return text;
  }

  public String convertBase64EncodedStringToDocFileAndExtractText(String docFileBase64BitEncoded,
      String fileName) throws IOException {
    byte[] docAsBytes = Base64.getDecoder().decode(docFileBase64BitEncoded);
    Path path = Files.createTempFile(fileName, ".docx");
    Files.write(path, docAsBytes);
    XWPFDocument doc = new XWPFDocument(Files.newInputStream(path));
    XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(doc);
    String docText = xwpfWordExtractor.getText();
    long count = Arrays.stream(docText.split("\\s+")).count();
    logger.info("Total words: " + count);
    logger.info("Extracted test from PDF document formed from encoded base64 string " + docText);
    xwpfWordExtractor.close();
    return docText;
  }

}
