package com.ynov.dietynov;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RVAdapterRecipes extends RecyclerView.Adapter<RVAdapterRecipes.RVViewHolderRecipes> {

    private ArrayList<Recipe> listRecipes;
    private Context mContext;

    public static class RVViewHolderRecipes extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView iv_recipeImage;
        public TextView tv_recipeName;
        public LinearLayout parentLayout;

        public RVViewHolderRecipes(View v) {
            super(v);
            //Ajouter les vues
            iv_recipeImage = v.findViewById(R.id.IV_recipe);
            tv_recipeName = v.findViewById(R.id.TV_recipe);

            parentLayout = v.findViewById(R.id.LO_recyclerview);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RVAdapterRecipes(Context _context, ArrayList<Recipe> _myDataset) {
        this.mContext = _context;
        listRecipes = _myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RVAdapterRecipes.RVViewHolderRecipes onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_recipe, parent, false);

        RVAdapterRecipes.RVViewHolderRecipes vh = new RVAdapterRecipes.RVViewHolderRecipes(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RVAdapterRecipes.RVViewHolderRecipes holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Recipe mRecipe = listRecipes.get(position);
        holder.tv_recipeName.setText(mRecipe.getName());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext, "Nom : " + mRecipe.getName(), Toast.LENGTH_SHORT).show();

                //Instanciation de l'intent
//                Intent intentInfo = new Intent(mContext, FicheProspectActivity.class);
//                intentInfo.putExtra("Prospect", prospect);
                //Démarrage de l'activité de fiche d'info du prospect
//                mContext.startActivity(intentInfo);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listRecipes.size();
    }
}
