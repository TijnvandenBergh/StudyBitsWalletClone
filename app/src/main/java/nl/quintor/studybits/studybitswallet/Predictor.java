package nl.quintor.studybits.studybitswallet;

import android.util.Log;

public class Predictor {

    public boolean credentialToReadable(String crendentialValue) {
        if(crendentialValue.contains("Transcript")) {
            Log.e("TranscriptValue", crendentialValue);
            return true;
        }
        return false;
    }
}
