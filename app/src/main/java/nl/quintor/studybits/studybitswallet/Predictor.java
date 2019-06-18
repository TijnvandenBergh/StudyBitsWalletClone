package nl.quintor.studybits.studybitswallet;

import android.util.Log;

public class Predictor {

    public String credentialToReadable(String crendentialValue) {
        if(crendentialValue.contains("Transcript")) {
            Log.e("TranscriptValue", crendentialValue);
        }
        return crendentialValue;
    }
}
