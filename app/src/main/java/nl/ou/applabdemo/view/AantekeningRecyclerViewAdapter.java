package nl.ou.applabdemo.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nl.ou.applabdemo.R;
import nl.ou.applabdemo.domain.Aantekening;

@SuppressWarnings("SpellCheckingInspection")
/**
 * Adapter om de meldingen weer te kunnen geven in een recyclerview
 */
public class AantekeningRecyclerViewAdapter extends RecyclerView.Adapter<AantekeningRecyclerViewAdapter.BehandelaarViewHolder> {

    private static final String TAG = AantekeningRecyclerViewAdapter.class.getSimpleName();

    private List<Aantekening> mlijstAantekeningen;
    private Context mContext;

    /**
     * Constructor
     * @param lijstAantekeningen Lijst met aantekeningen
     * @param mContext Contetxt
     */
    public AantekeningRecyclerViewAdapter( List<Aantekening> lijstAantekeningen, Context mContext) {
        this.mlijstAantekeningen = lijstAantekeningen;
        this.mContext = mContext;
    }

    /**
     * BinnenKlasse die verantwoordelijk is voor het creeren van ViewHolder objecten
     */
    public class BehandelaarViewHolder extends RecyclerView.ViewHolder {

        TextView aantekeningTekst;
        TextView aantekeningDatumTijd;
        ConstraintLayout aantekeningenLayout;

        public BehandelaarViewHolder(View itemView) {
            super(itemView);

            aantekeningTekst = itemView.findViewById(R.id.aantekening_text);
            aantekeningDatumTijd = itemView.findViewById(R.id.aantekening_datumTijd);
            aantekeningenLayout = itemView.findViewById(R.id.aantekening_layout);
        }
    }


    @NonNull
    @Override
    /**
     * Creert viewholder voor het afbeelden van een aantekening
     */
    public AantekeningRecyclerViewAdapter.BehandelaarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.aantekening_lijst_item, viewGroup, false);
        BehandelaarViewHolder viewHolder = new BehandelaarViewHolder(view);

        return viewHolder;
    }

    @Override
    /**
     * Vult de viewholder met data van een aantekening
     */
    public void onBindViewHolder(@NonNull AantekeningRecyclerViewAdapter.BehandelaarViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: aangeroepen.");

        holder.aantekeningTekst.setText(mlijstAantekeningen.get(position).getTekst());
        holder.aantekeningDatumTijd.setText(mlijstAantekeningen.get(position).getDatumTijd());
    }


    @Override
    /**
     * Telt het aantal aantekeningen in een melding
     */
    public int getItemCount() {
        return mlijstAantekeningen.size();
    }

}
