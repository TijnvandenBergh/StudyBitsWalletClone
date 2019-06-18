package nl.quintor.studybits.studybitswallet.university;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;

import java.util.ArrayList;

import nl.quintor.studybits.indy.wrapper.message.IndyMessageTypes;
import nl.quintor.studybits.studybitswallet.IndyClient;
import nl.quintor.studybits.studybitswallet.R;
import nl.quintor.studybits.studybitswallet.WalletActivity;
import nl.quintor.studybits.studybitswallet.exchangeposition.StudyBitsMessageTypes;
import nl.quintor.studybits.studybitswallet.room.AppDatabase;
import nl.quintor.studybits.studybitswallet.room.entity.University;

public class UniversityActivity extends WalletActivity {
    ArrayList<String> universities = new ArrayList<>();
    public static final String EXTRA_CONNECTION_REQUEST = "nl.quintor.studybits.indy.wrapper.dto.ConnectionRequest";

    private RecyclerView universityRecyclerView;
    private UniversityRecyclerViewAdapter universityAdapter;
    private RecyclerView.LayoutManager universityLayoutManager;
    private UniversityListViewModel universityListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        IndyMessageTypes.init();
        StudyBitsMessageTypes.init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final UniversityActivity activity = this;

        Intent intent = getIntent();

        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();

            String name = data.getQueryParameter("university");
            String did = data.getQueryParameter("did");
            String endpoint = data.getQueryParameter("endpoint");

            ConnectUniversityDialogFragment dialogFragment = new ConnectUniversityDialogFragment();
            Bundle arguments = new Bundle();
            arguments.putString("name", name);
            dialogFragment.setArguments(arguments);


            dialogFragment.setConnectDialogListener(() -> {
                String username = dialogFragment.getUsernameText();
                String password = dialogFragment.getPasswordText();
                Log.d("STUDYBITS", "Logging in with endpoint " + endpoint + " and username " + username);
                Log.d("STUDYBITS", studentWallet.getMainDid());
                IndyClient indyClient = new IndyClient(studentWallet, AppDatabase.getInstance(getApplicationContext()));


                try {
                    Log.i("STUDYBITS", endpoint);
                    Log.i("STUDYBITS", name);
                    University university = indyClient.connect(endpoint, name, username, password, did);

                    Snackbar.make(activity.getWindow().getDecorView(), "Connected to " + university.getName() + "!", Snackbar.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("STUDYBITS", "Exception on accepting connection request" + e.getLocalizedMessage());

                }
            });
            dialogFragment.show(getSupportFragmentManager(), "connect");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        universityRecyclerView = findViewById(R.id.university_recycler_view);

        universityLayoutManager = new LinearLayoutManager(this);
        universityRecyclerView.setLayoutManager(universityLayoutManager);

        universityAdapter = new UniversityRecyclerViewAdapter(this, new ArrayList<>());

        universityRecyclerView.setAdapter(universityAdapter);

        universityListViewModel = ViewModelProviders.of(this).get(UniversityListViewModel.class);

        universityListViewModel.getUniversityList().observe(UniversityActivity.this, universities -> {
            universityAdapter.setData(universities);
        });


    }


}
