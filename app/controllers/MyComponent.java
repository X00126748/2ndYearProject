package controllers;

import org.apache.commons.mail.EmailAttachment;
import play.Play;
import play.api.libs.mailer.MailerClient;
import play.libs.mailer.Email;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.File;

public class MyComponent extends Controller {

  private final MailerClient mailer;

  @Inject
  public MyComponent(MailerClient mailer) {
    this.mailer = mailer;
  }

  public Result send() {
    String cid = "1234";
    final Email email = new Email()
      .setSubject("Simple email")
      .setFrom("Mister FROM <irishboy5697@gmail.com>")
      .addTo("Miss TO <irishboy5697@gmail.com>")
     // .addAttachment("favicon.png", new File(Play.application().classloader().getResource("public/images/favicon.png").getPath(), cid))
      //.addAttachment("data.txt", "data".getBytes(), "text/plain", "Simple data", EmailAttachment.INLINE)
      .setBodyText("A text message")
      .setBodyHtml("<html><body><p>An <b>html</b> message with cid <img src=\"cid:" + cid + "\"></p></body></html>");
    String id = mailer.send(email);
    return ok("Email " + id + " sent!");
  }


  public Result sendEmail() {
    String cid = "1234";
    Email email = new Email()
      .setSubject("Simple email")
      .setFrom("Mister FROM <from@email.com>")
      .addTo("Miss TO <to@email.com>")
      // adds attachment
      .addAttachment("attachment.pdf", new File("/some/path/attachment.pdf"))
      // adds inline attachment from byte array
      .addAttachment("data.txt", "data".getBytes(), "text/plain", "Simple data", EmailAttachment.INLINE)
      // adds cid attachment
      .addAttachment("image.jpg", new File("/some/path/image.jpg"), cid)
      // sends text, HTML or both...
      .setBodyText("A text message")
      .setBodyHtml("<html><body><p>An <b>html</b> message with cid <img src=\"cid:" + cid + "\"></p></body></html>");
    mailer.send(email);
 return ok("Email  sent!");
  }



}
