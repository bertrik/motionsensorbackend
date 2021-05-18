package nl.bertriksikken.ttnv3.enddevice;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Communicates with the TTN v3 device registry API.
 */
public final class EndDeviceRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(EndDeviceRegistry.class);

    private final IEndDeviceRegistryRestApi restApi;
    private final String applicationId;
    private final String authToken;

    public EndDeviceRegistry(IEndDeviceRegistryRestApi restApi, String applicationId, String apiKey) {
        this.restApi = restApi;
        this.applicationId = applicationId;
        this.authToken = "Bearer " + apiKey;
    }

    public static IEndDeviceRegistryRestApi newRestClient(String url, Duration timeout) {
        LOG.info("Creating new REST client for '{}' with timeout {}", url, timeout);

        OkHttpClient client = new OkHttpClient().newBuilder().callTimeout(timeout).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create()).client(client).build();
        return retrofit.create(IEndDeviceRegistryRestApi.class);
    }

    public List<EndDevice> listEndDevices() throws IOException {
        String fields = String.join(",", IEndDeviceRegistryRestApi.FIELD_IDS,
                IEndDeviceRegistryRestApi.FIELD_ATTRIBUTES);
        Response<EndDevices> response = restApi.listEndDevices(authToken, applicationId, fields).execute();
        if (!response.isSuccessful()) {
            LOG.warn("Request failed: {} - {}", response.message(), response.errorBody().string());
        }
        EndDevices endDevices = response.body();
        return endDevices.getEndDevices();
    }

}
