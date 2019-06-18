package nl.quintor.studybits.studybitswallet.credential;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import android.util.Log;

import org.hyperledger.indy.sdk.IndyException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import nl.quintor.studybits.indy.wrapper.IndyWallet;
import nl.quintor.studybits.indy.wrapper.Prover;

import nl.quintor.studybits.indy.wrapper.dto.CredentialInfo;
import nl.quintor.studybits.indy.wrapper.dto.CredentialOffer;
import nl.quintor.studybits.indy.wrapper.message.MessageEnvelopeCodec;
import nl.quintor.studybits.studybitswallet.AgentClient;
import nl.quintor.studybits.studybitswallet.room.entity.University;

import static nl.quintor.studybits.studybitswallet.TestConfiguration.STUDENT_SECRET_NAME;

public class CredentialOfferViewModel extends AndroidViewModel {
    private final MutableLiveData<List<CredentialOrOffer>> credentialOffers = new MutableLiveData<>();
    private final MutableLiveData<List<CredentialInfo>> credentials = new MutableLiveData<>();

    public CredentialOfferViewModel(@NonNull Application application) {
        super(application);
    }

    public void initCredentials(IndyWallet indyWallet) {
        Prover prover = new Prover(indyWallet, STUDENT_SECRET_NAME);

        try {
            credentials.setValue(prover.findAllCredentials().get());
        } catch (InterruptedException | ExecutionException | IndyException e) {
            Log.e("STUDYBITS", "Error while refreshing credentials");
            e.printStackTrace();
        }
    }

    public void initCredentialOffers(List<University> universities, MessageEnvelopeCodec codec) {
        if (universities == null) {
            return;
        }
        Log.d("STUDYBITS", "Initializing credential offers");
        try {
            List<CredentialOrOffer> credentialOrOffers = new ArrayList<>();
            for (University university : universities) {
                Log.d("STUDYBITS", "Initializing credential offers for university " + university);
                List<CredentialOrOffer> credentialOrOffersForUni = getCredentialOrOffers(codec, university);

                credentialOrOffers.addAll(credentialOrOffersForUni);
            }


            credentialOffers.setValue(credentialOrOffers);
        }
        catch (Exception e) {
            Log.e("STUDYBITS", "Exception while getting credential offers" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private List<CredentialOrOffer> getCredentialOrOffers(MessageEnvelopeCodec codec, University university) throws IOException, InterruptedException, ExecutionException, IndyException {

        List<CredentialOffer> offersForUni = new AgentClient(university, codec).getCredentialOffers();

        Log.d("STUDYBITS", "Got " + offersForUni.size() + " message envelopes with offers");

        return offersForUni.stream()
                .map(credentialOffer -> CredentialOrOffer.fromCredentialOffer(university, credentialOffer))
                .collect(Collectors.toList());
    }


    public LiveData<List<CredentialOrOffer>> getCredentialOffers() {
        return credentialOffers;
    }

    public LiveData<List<CredentialInfo>> getCredentials() {
        return credentials;
    }
}
