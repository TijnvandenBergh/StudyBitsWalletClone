package nl.quintor.studybits.studybitswallet;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.apache.commons.io.IOUtils;
import org.hyperledger.indy.sdk.IndyException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import nl.quintor.studybits.indy.wrapper.dto.ConnectionRequest;
import nl.quintor.studybits.indy.wrapper.dto.ConnectionResponse;
import nl.quintor.studybits.indy.wrapper.dto.CredentialOffer;
import nl.quintor.studybits.indy.wrapper.dto.CredentialOfferList;
import nl.quintor.studybits.indy.wrapper.message.IndyMessageTypes;
import nl.quintor.studybits.indy.wrapper.message.MessageEnvelope;
import nl.quintor.studybits.indy.wrapper.message.MessageEnvelopeCodec;
import nl.quintor.studybits.indy.wrapper.message.MessageType;
import nl.quintor.studybits.studybitswallet.exchangeposition.AuthcryptableExchangePositions;
import nl.quintor.studybits.studybitswallet.exchangeposition.ExchangePosition;
import nl.quintor.studybits.studybitswallet.room.entity.University;
import android.util.Base64;

import static nl.quintor.studybits.indy.wrapper.message.IndyMessageTypes.*;
import static nl.quintor.studybits.studybitswallet.exchangeposition.StudyBitsMessageTypes.EXCHANGE_POSITIONS;

public class AgentClient {
    public static Map<String, CookieManager> cookieManagers= new HashMap<>();

    private University university;
    private MessageEnvelopeCodec codec;

    public AgentClient(University university, MessageEnvelopeCodec codec) {
        this.university = university;
        this.codec = codec;
    }
    public static MessageEnvelope<ConnectionResponse> login(String endpoint, String username, String password, MessageEnvelope<ConnectionRequest> envelope) throws Exception {
        try {
            Log.d("STUDYBITS", "Logging in");
            String encoded = "";
            URL url = new URL(endpoint + "/agent/login");
            if(!username.isEmpty()) {
                String credentials = username+":"+password;
                encoded = Base64.encodeToString(credentials.getBytes(StandardCharsets.UTF_8), 0);
            } else {
                String credentials = ":";
                encoded = Base64.encodeToString(credentials.getBytes(StandardCharsets.UTF_8), 0);
            }
            CookieManager cookieManager = cookieManagers.computeIfAbsent(endpoint, s -> new CookieManager());
            CookieHandler.setDefault(cookieManager);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic "+encoded);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);



            OutputStream out = urlConnection.getOutputStream();
            out.write(envelope.toJSON().getBytes(Charset.forName("utf8")));
            out.close();

            if(urlConnection.getResponseCode() == 200) {
                return MessageEnvelope.parseFromString(IOUtils.toString(urlConnection.getInputStream(), Charset.forName("utf8")), IndyMessageTypes.CONNECTION_RESPONSE);
            } else if(urlConnection.getResponseCode() == 403) {
                throw new IllegalAccessException("Access denied for student " + username);
            } else {
                throw new Exception();
            }
        }
        catch (IOException e) {
            Log.e("STUDYBITS", "Exception when logging in" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<CredentialOffer> getCredentialOffers() throws IOException, IndyException, ExecutionException, InterruptedException {

        MessageEnvelope<CredentialOfferList> credentialOfferListEnvelope = this.postAndReturnMessage(getRequestEnvelope(CREDENTIAL_OFFERS), CREDENTIAL_OFFERS);

        CredentialOfferList offersList = codec.decryptMessage(credentialOfferListEnvelope).get();

        List<CredentialOffer> credentialOffers = offersList.getCredentialOffers();

        return credentialOffers;
    }

    public List<ExchangePosition> getExchangePositions() throws IOException, IndyException, ExecutionException, InterruptedException {

        MessageEnvelope<AuthcryptableExchangePositions> exchangePositionsMessageEnvelope = this.postAndReturnMessage(getRequestEnvelope(EXCHANGE_POSITIONS), EXCHANGE_POSITIONS);

        AuthcryptableExchangePositions exchangePositionsList = codec.decryptMessage(exchangePositionsMessageEnvelope).get();

        List<ExchangePosition> exchangePositions = exchangePositionsList.getExchangePositions();

        exchangePositions.forEach(exchangePosition -> {
            exchangePosition.setUniversity(university);
        });

        return exchangePositions;
    }

    public void postMessage(MessageEnvelope message) throws IOException {
        HttpURLConnection urlConnection = getConnection("/agent/message");

        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(false);
        urlConnection.setDoInput(true);

        OutputStream out = urlConnection.getOutputStream();
        out.write(message.toJSON().getBytes(Charset.forName("utf8")));
        out.close();

        Log.d("STUDYBITS", "Response code: " + urlConnection.getResponseCode());
    }

    public MessageEnvelope postAndReturnMessage(MessageEnvelope message, MessageType returnType) throws IOException {
        HttpURLConnection urlConnection = getConnection("/agent/message");

        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        OutputStream out = urlConnection.getOutputStream();
        out.write(message.toJSON().getBytes(Charset.forName("utf8")));
        out.close();

        Log.d("STUDYBITS", "Response code: " + urlConnection.getResponseCode());
        if(returnType != null ) {
            return MessageEnvelope.parseFromString(IOUtils.toString(urlConnection.getInputStream(), Charset.forName("utf8")), returnType);
        } else {
            return null;
        }

    }

    public MessageEnvelope<String> getRequestEnvelope(MessageType expectedReturn) throws JsonProcessingException, IndyException, ExecutionException, InterruptedException {
        return codec.encryptMessage(expectedReturn.getURN(), GET_REQUEST, university.getTheirDid()).get();
    }

    public HttpURLConnection getConnection(String path) throws IOException {
        CookieManager cookieManager = cookieManagers.computeIfAbsent(university.getEndpoint(), s -> new CookieManager());
        CookieHandler.setDefault(cookieManager);
        URL url = new URL(university.getEndpoint() + path);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Content-Type", "application/json");

        return urlConnection;
    }
}
