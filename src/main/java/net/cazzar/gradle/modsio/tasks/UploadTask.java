package net.cazzar.gradle.modsio.tasks;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.gradle.api.internal.AbstractTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import javax.net.ssl.SSLHandshakeException;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadTask extends AbstractTask {
    @Input
    public File artifact = null;
    @Input
    public String changelog = "";
    @Input
    public String tag = "release";
    @Input
    public String apiKey;
    @Input
    public String modId;
    @Input
    public String minecraft;
    @Input
    public boolean current = true;

    private SSLConnectionSocketFactory setupSSL() {
        SSLContextBuilder builder = new SSLContextBuilder();
        try {
            builder.loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            });
            return new SSLConnectionSocketFactory(builder.build());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TaskAction
    public void uploadToModsIO() {

        Logger logger = getProject().getLogger();

        HttpClient client = HttpClientBuilder.create()
                .setSSLSocketFactory(setupSSL())
                .build();
        HttpPost post = new HttpPost(String.format("https://mods.io/mods/%s/versions/create.json", modId));

        Artifact data = new Artifact(this.artifact.getName(),
                getProject().getVersion().toString(),
                minecraft,
                changelog,
                tag,
                current
        );

        logger.debug("Sending post with + " + data);

        post.addHeader("X-API-Key", apiKey);
        post.addHeader("Accept", "application/json");

        HttpEntity entity = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addTextBody("body", new Gson().toJson(data))
                .addPart("file", new FileBody(artifact))
                .build();

        post.setEntity(entity);

        try {
            HttpResponse response = client.execute(post);
            HttpEntity ent = response.getEntity();

            if (response.getStatusLine().getStatusCode() != 200) {
                Map<String, List<String>> error = new HashMap<String, List<String>>();
                //noinspection unchecked
                error = (Map<String, List<String>>) new Gson().fromJson(EntityUtils.toString(ent), error.getClass());
                throw new RuntimeException(Arrays.toString(error.get("Errors").toArray()));
            }
        } catch (SSLHandshakeException ex) {
            throw new RuntimeException(ex);
        } catch (IOException thowable) {
            thowable.printStackTrace();

        }
    }

    private static class Artifact {
        private Artifact(String filename, String name, String minecraft, String changelog, String tag, boolean current) {
            this.filename = filename;
            this.version = new Version();
            version.name = name;
            version.minecraft = minecraft;
            version.changelog = changelog;
            version.tag = tag;
            this.current = current;
        }

        @Override
        public String toString() {
            return "Artifact{" +
                    "filename='" + filename + '\'' +
                    ", version=" + version +
                    ", current=" + current +
                    '}';
        }

        public static class Version {
            String name, minecraft, changelog, tag;

            @Override
            public String toString() {
                return "Version{" +
                        "name='" + name + '\'' +
                        ", minecraft='" + minecraft + '\'' +
                        ", changelog='" + changelog + '\'' +
                        ", tag='" + tag + '\'' +
                        '}';
            }
        }

        String filename;
        Version version;
        boolean current;


    }
}
