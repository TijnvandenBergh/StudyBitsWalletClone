package nl.quintor.studybits.studybitswallet.credential;

import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl.quintor.studybits.indy.wrapper.dto.CredentialOffer;
import nl.quintor.studybits.studybitswallet.ExpandableTextView;
import nl.quintor.studybits.studybitswallet.R;
import nl.quintor.studybits.studybitswallet.credential.CredentialFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link CredentialOffer} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class CredentialRecyclerViewAdapter extends RecyclerView.Adapter<CredentialRecyclerViewAdapter.ViewHolder> {

    private List<CredentialOrOffer> credentials;
    private final OnListFragmentInteractionListener credentialInteractionListener;

    public CredentialRecyclerViewAdapter(List<CredentialOrOffer> items, OnListFragmentInteractionListener listener) {
        credentials = items;
        credentialInteractionListener = listener;

    }
    public void Test() {

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_credential, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.credentialOffer = credentials.get(position);
        holder.mIdView.setText(credentials.get(position).getUniversityName());
        if (holder.credentialOffer.getCredential() != null) {
           Map<String, String> credential = holder.credentialOffer.getCredential().getAttrs();
           Log.e("INHOUD", credential.toString());

             //Setting the diploma
             holder.mDiplomaView.setVisibility(View.VISIBLE);
             holder.mDiplomaDisplayView.setVisibility(View.VISIBLE);
             holder.mDiplomaDisplayView.setText(credential.get("degree"));

             //Setting the full name of diploma
            holder.mNameView.setVisibility(View.VISIBLE);
            holder.mNameDisplayView.setVisibility(View.VISIBLE);
            holder.mNameDisplayView.setText(credential.get("full_name"));

             //Setting the datum of diploma
            holder.mDatumView.setVisibility(View.VISIBLE);
            holder.mDatumDisplayView.setVisibility(View.VISIBLE);
            holder.mDatumDisplayView.setText(credential.get("completiondate"));

            holder.mTypeView.setVisibility(View.VISIBLE);
            holder.mTypeDisplayView.setVisibility(View.VISIBLE);
            holder.mTypeDisplayView.setText(credential.get("type"));

            //Setting the EC of diploma
            holder.mEcView.setVisibility(View.VISIBLE);
            holder.mEcDisplayView.setVisibility(View.VISIBLE);
            holder.mEcDisplayView.setText(credential.get("totalec"));
            //Image if proven
            holder.mProvenImageView.setVisibility(View.VISIBLE);

            //Settings the grades and textview
            holder.mResultsView.setVisibility(View.VISIBLE);
            holder.mResultsView.setPaintFlags(holder.mResultsView.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
            holder.mExpandableTextView.setVisibility(View.VISIBLE);
            holder.mExpandableTextView.setText(credential.get("courses"));


            holder.mDisplayDiplomaTypeLayout.setVisibility(View.VISIBLE);
            holder.mDisplayDiplomaLayout.setVisibility(View.VISIBLE);
            holder.mDisplayNameLayout.setVisibility(View.VISIBLE);
            holder.mDisplayDateLayout.setVisibility(View.VISIBLE);

             if (holder.mContentView.getText().length   () == 0) {
                 holder.mContentView.setVisibility(View.GONE);
             }
//            String text = holder.credentialOffer.getCredential().getAttrs()
//                    .entrySet()
//                    .stream()
//                    .map(entry -> entry.getKey() + ": " + entry.getValue())
//                    .collect(Collectors.joining("\n"));
//            holder.mContentView.setText(text);
        }
        else {
            holder.mContentView.setText(holder.credentialOffer.getValue());
        }



        if (holder.credentialOffer.getCredentialOffer() != null) {
            holder.mView.setBackgroundColor(ContextCompat.getColor(holder.mView.getContext(), R.color.colorCredentialOffer));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != credentialInteractionListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    credentialInteractionListener.onListFragmentInteraction(holder.credentialOffer);

                }
            }
        });
    }

    public void setDataset(List<CredentialOrOffer> credentialOrOffers) {
        this.credentials = credentialOrOffers;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return credentials.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        //Display view diploma
        public final TextView mDiplomaDisplayView;
        public final TextView mDiplomaView;
        //Display view names
        public final TextView mNameView;
        public final TextView mNameDisplayView;
        //Display view Datum received
        public final TextView mDatumView;
        public final TextView mDatumDisplayView;
        //Display the total ec
        public final TextView mEcView;
        public final TextView mEcDisplayView;

        //Display view results text
        public final TextView mResultsView;

        //Display the type of diploma
        public final TextView mTypeView;
        public final TextView mTypeDisplayView;

        public final ExpandableTextView mExpandableTextView;
        public final ImageView mProvenImageView;

        //Getting the linearlayouts
        public final LinearLayout mDisplayDiplomaTypeLayout;
        public final LinearLayout mDisplayDiplomaLayout;
        public final LinearLayout mDisplayNameLayout;
        public final LinearLayout mDisplayDateLayout;
        public final LinearLayout mDisplayECLayout;

        public CredentialOrOffer credentialOffer;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            mDiplomaView = (TextView) view.findViewById(R.id.diplomaView);
            mDiplomaDisplayView = (TextView) view.findViewById(R.id.diplomaDisplayView);
            mNameView = (TextView) view.findViewById(R.id.nameVIew);
            mNameDisplayView = (TextView) view.findViewById(R.id.nameDisplayView);
            mDatumView = (TextView) view.findViewById(R.id.datumView);
            mDatumDisplayView = (TextView) view.findViewById(R.id.datumDisplayView);
            mEcView = (TextView) view.findViewById(R.id.ecView);
            mEcDisplayView = (TextView) view.findViewById(R.id.ecDisplayView);
            mTypeView = (TextView) view.findViewById(R.id.typeView);
            mTypeDisplayView = (TextView) view.findViewById(R.id.typeDisplayView);
            mResultsView = (TextView) view.findViewById(R.id.resultsTextView);
            mExpandableTextView = (ExpandableTextView) view.findViewById(R.id.resultsExpandedView);
            mProvenImageView = (ImageView) view.findViewById(R.id.provenImageView);

            //Layouts
            mDisplayDiplomaTypeLayout = (LinearLayout) view.findViewById(R.id.displayDiplomaTypeLayout);
            mDisplayDiplomaLayout = (LinearLayout) view.findViewById(R.id.displayDiplomaLayout);
            mDisplayNameLayout = (LinearLayout) view.findViewById(R.id.displayNameLayout);
            mDisplayDateLayout = (LinearLayout) view.findViewById(R.id.displayDateLayout);
            mDisplayECLayout = (LinearLayout) view.findViewById(R.id.displayEClayout);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
