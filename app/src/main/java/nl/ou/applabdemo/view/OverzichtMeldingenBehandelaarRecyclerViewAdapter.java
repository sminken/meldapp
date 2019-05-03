package nl.ou.applabdemo.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nl.ou.applabdemo.R;
import nl.ou.applabdemo.domain.GebruikerInfo;
import nl.ou.applabdemo.domain.MeldingInfo;
import nl.ou.applabdemo.util.StatusVertaler;

/**
 * Adapter om de meldingen weer te kunnen geven in een recyclerview
 */
@SuppressWarnings("SpellCheckingInspection")
public class OverzichtMeldingenBehandelaarRecyclerViewAdapter extends RecyclerView.Adapter<OverzichtMeldingenBehandelaarRecyclerViewAdapter.MelderViewHolder>{

    private static final String TAG = OverzichtMeldingenBehandelaarRecyclerViewAdapter.class.getSimpleName();

    private List<MeldingInfo> meldingenInfo;
    private List<GebruikerInfo> meldersInfo;
    private String meldingGroep;
    private Context mContext;

    public OverzichtMeldingenBehandelaarRecyclerViewAdapter(List<MeldingInfo> meldingenInfo,
                                                            List<GebruikerInfo> meldersInfo,
                                                            String meldingGroep,
                                                            Context mContext) {

        this.meldingenInfo = meldingenInfo;
        this.meldersInfo = meldersInfo;
        this.meldingGroep = meldingGroep;
        this.mContext = mContext;
    }

    /**
     * Retourneert een viewHolder object
     *
     * @param viewGroup De groep van de view
     * @param viewType Het type van de view
     * @return viewHolder object
     */
    @Override
    public OverzichtMeldingenBehandelaarRecyclerViewAdapter.MelderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_overzicht_melding_behandelaar, viewGroup, false);
        return new MelderViewHolder(view);
    }

    /**
     * Wordt aangeroepen door RecyclerView om de data te binden aan juiste positie
     *
     * @param holder De holder.
     * @param position De positie.
     */
    @Override
    public void onBindViewHolder(@NonNull final MelderViewHolder holder, final int position) {

        // Vul de gegevens van de melding.
        holder.onderwerp.setText(meldingenInfo.get(position).getOnderwerp());
        String datum = meldingenInfo.get(position).getDatum();
        holder.datum.setText(datum);
        holder.status.setText(StatusVertaler.vertaalStatus(meldingenInfo.get(position).getStatusTekst()));
        holder.inhoud.setText(meldingenInfo.get(position).getInhoud());

        // Haal de gegevens van deze melder.
        String opgemaakteNaam = null;
        String telefoonnummer = null;
        String uidGebruikerMelder = meldingenInfo.get(position).getUidGebruikerMelder();
        GebruikerInfo melderInfo = searchMelder(uidGebruikerMelder);
        if (melderInfo != null) {
            opgemaakteNaam = melderInfo.getOpgemaakteNaam();
            telefoonnummer = melderInfo.getTelefoonnummer();
        }

        holder.melder.setText(opgemaakteNaam);
//        holder.telefoonnummer.setText(telefoonnummer);

//        Verbindt de onClickListener aan de viewHolder
        //noinspection Convert2Lambda
        holder.meldingenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, MeldingDetailsBehandelaarActivity.class);

                intent.putExtra("melding_groep", meldingGroep);

                MeldingInfo meldingInfo = meldingenInfo.get(position);
                String uidMelding = meldingInfo.getUidMelding();
                intent.putExtra("uid_melding", uidMelding);

                mContext.startActivity(intent);
            }
        });
    }

    /**
     * Zoekt de gebruikers informatie van de gegeven melder identificatie.
     *
     * @param uidGebruikerMelder De identificatie van de melder.
     * @return De gebruiker informatie van de gevraagde melder als deze is gevonden. Anders null.
     */
    private GebruikerInfo searchMelder(String uidGebruikerMelder) {

        GebruikerInfo gebruikerInfo = null;

        for(GebruikerInfo melderInfo: meldersInfo) {
            String uidMelder = melderInfo.getUid();
            if (uidMelder.equals(uidGebruikerMelder)) {
                gebruikerInfo = melderInfo;
                break;
            }
        }

        return gebruikerInfo;
    }

    //    Retourneert het aantal meldingen in de ArrayList
    @Override
    public int getItemCount() {
        return meldingenInfo.size();
    }

    //    BinnenKlasse die verantwoordelijk is voor het creeren van ViewHolder objecten
    public class MelderViewHolder extends RecyclerView.ViewHolder {


        TextView onderwerp;
        TextView datum;
        TextView melder;
        TextView telefoonnummer;
        TextView status;
        TextView inhoud;
        ConstraintLayout meldingenLayout;

        public MelderViewHolder(View itemView) {
            super(itemView);

            onderwerp = itemView.findViewById(R.id.content_onderwerp);
            datum = itemView.findViewById(R.id.content_datum);
            melder = itemView.findViewById(R.id.content_melder);
            telefoonnummer = itemView.findViewById(R.id.content_telefoonnummer);
            inhoud = itemView.findViewById(R.id.content_inhoud);
            status = itemView.findViewById(R.id.content_status);
            meldingenLayout = itemView.findViewById(R.id.layout_meldingen);
        }
    }
}
