package com.ynov.dietynov;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RVAdapaterMeasurements extends RecyclerView.Adapter<RVAdapaterMeasurements.RVViewHolderMeasurements> {

    private ArrayList<MeasurementData> listMeasurementData;
    private Context mContext;
    private String unit;

    public static class RVViewHolderMeasurements extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_date;
        public TextView tv_measurement;
        public LinearLayout parentLayout;

        public RVViewHolderMeasurements(View v) {
            super(v);
            //Ajouter les vues
            tv_date = v.findViewById(R.id.TV_date);
            tv_measurement = v.findViewById(R.id.TV_measurement);

            parentLayout = v.findViewById(R.id.LO_recyclerview);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RVAdapaterMeasurements(Context _context, ArrayList<MeasurementData> _myDataset, String _unit) {
        this.mContext = _context;
        listMeasurementData = _myDataset;
        this.unit = _unit;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RVAdapaterMeasurements.RVViewHolderMeasurements onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);

        RVViewHolderMeasurements vh = new RVViewHolderMeasurements(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RVViewHolderMeasurements holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final MeasurementData mData = listMeasurementData.get(position);
        long yourSeconds = mData.getDate() + 1546300800;
        Date d = new Date(yourSeconds * 1000);
        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
        holder.tv_date.setText(df.format(d));
        holder.tv_measurement.setText(Float.toString(mData.getValue()) + " " + unit);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listMeasurementData.size();
    }
}