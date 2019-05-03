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
import nl.ou.applabdemo.domain.MeldingInfo;
import nl.ou.applabdemo.util.StatusVertaler;

/**
 * Adapter om de meldingen weer te kunnen geven in een recyclerview
 */
@SuppressWarnings("SpellCheckingInspection")
public class OverzichtMeldingenMelderRecyclerViewAdapter extends RecyclerView.Adapter<OverzichtMeldingenMelderRecyclerViewAdapter.MelderViewHolder>{

    private static final String TAG = OverzichtMeldingenBehandelaarRecyclerViewAdapter.class.getSimpleName();

    private List<MeldingInfo> meldingenInfo;
    private String meldingGroep;
    private Context mContext;

    public OverzichtMeldingenMelderRecyclerViewAdapter(List<MeldingInfo> meldingenInfo, String meldingGroep, Context mContext) {

        this.meldingenInfo = meldingenInfo;
        this.meldingGroep = meldingGroep;
        this.mContext = mContext;
    }

    /**
     * Retourneert een viewHolder object
     * @param viewGroup de groep Views
     * @param viewType
     * @return viewHolder object
     */
    @Override
    public OverzichtMeldingenMelderRecyclerViewAdapter.MelderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_overzicht_melding_melder, viewGroup, false);
        MelderViewHolder viewHolder = new MelderViewHolder(view);

        return viewHolder;
    }

    /**
     * Wordt aangeroepen door RecyclerView om de data te binden aan juiste positie
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull final MelderViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: aangeroepen.");

        holder.onderwerp.setText(meldingenInfo.get(position).getOnderwerp());
        holder.inhoud.setText(meldingenInfo.get(position).getInhoud());
        String datum = meldingenInfo.get(position).getDatumTijd().substring(0,10);
        holder.datum.setText(datum);
        holder.status.setText(StatusVertaler.vertaalStatus(meldingenInfo.get(position).getStatusTekst()));

        Log.d("bindviewholder", "hier gaat het mis " + holder);

//        Verbindt de onClickListener aan de viewHolder
        holder.meldingenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: geklikt op" + meldingenInfo.get(position));

                Intent intent = new Intent(mContext, MeldingDetailsMelderActivity.class);

                intent.putExtra("melding_groep", meldingGroep);

                MeldingInfo meldingInfo = meldingenInfo.get(position);
                String uidMelding = meldingInfo.getUidMelding();
                intent.putExtra("uid_melding", uidMelding);

                mContext.startActivity(intent);
            }
        });
    }

    //    Retourneert het aantal meldingen in de ArrayList
    @Override
    public int getItemCount() {
        return meldingenInfo.size();
    }

    //    BinnenKlasse die verantwoordelijk is voor het creeren van ViewHolder objecten
    public class MelderViewHolder extends RecyclerView.ViewHolder {


        TextView onderwerp;
        TextView inhoud;
        TextView datum;
        TextView status;
        ConstraintLayout meldingenLayout;

        public MelderViewHolder(View itemView) {
            super(itemView);

            onderwerp = itemView.findViewById(R.id.content_onderwerp);
            datum = itemView.findViewById(R.id.content_datum);
            inhoud = itemView.findViewById(R.id.content_inhoud);
            status = itemView.findViewById(R.id.content_status);
            meldingenLayout = itemView.findViewById(R.id.layout_meldingen);
        }
    }
}
