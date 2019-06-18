package nl.quintor.studybits.studybitswallet.exchangeposition;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import nl.quintor.studybits.indy.wrapper.message.IndyMessageTypes;
import nl.quintor.studybits.studybitswallet.R;

public class ExchangePositionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        IndyMessageTypes.init();
        StudyBitsMessageTypes.init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_position);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
