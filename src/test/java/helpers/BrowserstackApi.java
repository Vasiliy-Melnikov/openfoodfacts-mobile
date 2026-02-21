package helpers;

import config.BrowserstackConfig;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;

import static io.restassured.RestAssured.given;

public class BrowserstackApi {

    private final BrowserstackConfig cfg;

    public BrowserstackApi() {
        this.cfg = ConfigFactory.create(BrowserstackConfig.class, System.getProperties());
        RestAssured.baseURI = "https://api.browserstack.com";
    }

    public void attachVideo(String sessionId) {
        String user = resolveUser();
        String key = resolveKey();

        if (user.isBlank() || key.isBlank()) return;
        if (sessionId == null || sessionId.isBlank()) return;

        String videoUrl = given()
                .auth().preemptive().basic(user, key)
                .when()
                .get("/app-automate/sessions/" + sessionId + ".json")
                .then()
                .statusCode(200)
                .extract().path("automation_session.video_url");

        if (videoUrl != null && !videoUrl.isBlank()) {
            String html = "<html><body><video width='100%' height='100%' controls autoplay>" +
                    "<source src='" + videoUrl + "' type='video/mp4'></video></body></html>";
            Allure.addAttachment("Video", "text/html", html, "html");
        }
    }

    private String resolveUser() {
        if (cfg.user() != null && !cfg.user().isBlank()) return cfg.user();
        String u = System.getenv("BROWSERSTACK_USERNAME");
        if (u != null && !u.isBlank()) return u;
        u = System.getenv("BROWSERSTACK_USER");
        return u == null ? "" : u;
    }

    private String resolveKey() {
        if (cfg.key() != null && !cfg.key().isBlank()) return cfg.key();
        String k = System.getenv("BROWSERSTACK_ACCESS_KEY");
        if (k != null && !k.isBlank()) return k;
        k = System.getenv("BROWSERSTACK_KEY");
        return k == null ? "" : k;
    }
}