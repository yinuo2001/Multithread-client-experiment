
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class ClientAPI {
  private static final int threadGroupSize = 10;
  private static final int numThreadGroups = 10;
  private static final int delay = 2000;
  private static final String IPAddrTomcat = "http://54.200.14.239:8080/hw4a_war/albums";
  private static final String IPAddrGo = "http://54.200.14.239:8082/IGORTON/AlbumStore/1.0.0/albums";
//  private static final String output30 = "src/main/resources/output30.csv";

  private static final RequestConfig requestConfig = RequestConfig.custom()
          .setConnectTimeout(Timeout.ofSeconds(30))  // Timeout for initial connection
          .setResponseTimeout(Timeout.ofSeconds(30)) // Timeout for response
          .build();

  private static final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
  static {
    connManager.setMaxTotal(5000); // Set max total connections
    connManager.setDefaultMaxPerRoute(1000);
  }

  private static final CloseableHttpClient client = HttpClients.custom()
          .setConnectionManager(connManager)
          .setDefaultRequestConfig(requestConfig)
          .setRetryStrategy(new DefaultHttpRequestRetryStrategy(5, TimeValue.ofSeconds(2)))
          .build();

  private static void sendPostRequest() throws IOException {
    long start = System.currentTimeMillis();
    HttpPost postMethod = new HttpPost(IPAddrTomcat);
    File file = new File("src/main/resources/example.jpg");
    FileBody fileBody = new FileBody(file, ContentType.IMAGE_JPEG);

    StringBody profileBody = new StringBody(
            "{\"artist\":\"AgustD\",\"title\":\"D-Day\",\"year\":\"2023\"}",
            ContentType.APPLICATION_JSON
    );

    HttpEntity multipartEntity = MultipartEntityBuilder.create()
            .addPart("image", fileBody)
            .addPart("profile", profileBody)
            .build();

    postMethod.setEntity(multipartEntity);
//    client.execute(postMethod);
    try (CloseableHttpResponse response = client.execute(postMethod)) {
      EntityUtils.consume(response.getEntity());
    }
//    CloseableHttpResponse response = executeWithRetry(client, postMethod);
//    response.close();
    long end = System.currentTimeMillis();

//    Path path = Paths.get(output30);
//    StringBuilder builder = new StringBuilder();
//    builder.append("POST,").append(end - start).append(",").append(response.getCode()).append("\n");
//    Files.write(path, builder.toString().getBytes(), java.nio.file.StandardOpenOption.APPEND);
  }

  private static void sendGetRequest(int i) throws IOException {
//    File file = new File(output);
//    if (!file.exists()) {
//      file.createNewFile();
//    }

    long start = System.currentTimeMillis();
    HttpGet getMethod = new HttpGet(IPAddrTomcat + "/" + i);
    try (CloseableHttpResponse response = client.execute(getMethod)) {
      EntityUtils.consume(response.getEntity());
    }
//    CloseableHttpResponse response = executeWithRetry(client, getMethod);
//    response.close();
    long end = System.currentTimeMillis();
//    Path path = Paths.get(output30);
//    StringBuilder builder = new StringBuilder();
//    builder.append("GET,").append(end - start).append(",").append(response.getCode()).append("\n");
//    Files.write(path, builder.toString().getBytes(), java.nio.file.StandardOpenOption.APPEND);
  }

  public static void main(String[] args) throws IOException, InterruptedException {

    int totalRequests = numThreadGroups * threadGroupSize * 1000 ;
    CountDownLatch latch1 = new CountDownLatch(10);

    ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(500);
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(100);

    for (int i = 0; i < 10; i++) {
      executorService.submit(() -> {
        for (int j = 0; j < 100; j++) {
          try {
            sendPostRequest();
            sendGetRequest(0);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
        latch1.countDown();
      });
    }

    latch1.await();

    CountDownLatch latch2 = new CountDownLatch(threadGroupSize * numThreadGroups);
    long start = System.currentTimeMillis();
    for (int i = 0; i < numThreadGroups; i++) {
      int groupIndex = i;
      System.out.println("Starting thread group " + (groupIndex + 1));
      scheduler.schedule(() -> {
        for (int j = 0; j < threadGroupSize; j++) {
          int finalJ = j;
          executorService.submit(() -> {
            for (int k = 0; k < 1000; k++) {
              try {
                sendPostRequest();
                sendGetRequest(groupIndex * threadGroupSize + finalJ);
                System.out.println("Timestamp, "
                        + System.currentTimeMillis()
                        + ", Thread Group, "
                        + groupIndex
                        + ", ActiveThread, "
                        + executorService.getActiveCount());
                System.out.println("Timestamp, "
                        + System.currentTimeMillis()
                        + ", Thread Group, "
                        + groupIndex
                        + ", CompletedTask, "
                        + executorService.getCompletedTaskCount());
                System.out.println("Timestamp, "
                        + System.currentTimeMillis()
                        + ", Thread Group, "
                        + groupIndex
                        + ", Queue Size, "
                        + executorService.getQueue().size());
                System.out.println("Timestamp, "
                        + System.currentTimeMillis()
                        + ", Thread Group, "
                        + groupIndex
                        + ", Task Count, "
                        + executorService.getTaskCount());

              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            }

            latch2.countDown();

          });

        }
      }, groupIndex * delay, TimeUnit.MILLISECONDS);
    }
    latch2.await();
    long end = System.currentTimeMillis();

    scheduler.shutdown();
    executorService.shutdown();

    if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
      System.err.println("ExecutorService did not terminate in time!");
      executorService.shutdownNow(); // Force shutdown
    }

    if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
      System.err.println("Scheduler did not terminate in time!");
      scheduler.shutdownNow();
    }

    client.close();

    long wallTime = end - start;
    System.out.println("Wall Time: " + wallTime + "ms");
    int throughput = totalRequests / ((int) wallTime / 1000);
    System.out.println("Throughput: " + throughput + " requests/second");
  }
}
