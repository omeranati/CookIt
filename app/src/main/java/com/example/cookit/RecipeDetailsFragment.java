package com.example.cookit;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class RecipeDetailsFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "recipeId";
    String recipeId;


    // private OnFragmentInteractionListener mListener;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
   /* public static RecipeDetailsFragment newInstance(String param1, String param2) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        //TextView title = view.findViewById(R.id.);
        //title.setText(studentId);

        User a = new User("Omer Anati", "omer4554@gmail.com", "lala123");
        String[] ingredients = {"1kg Chicken", "2cups Canola oil", "1tbsp Salt"};
        String[] preparation = {"Defrost chicken", "Sprinkle salt", "Heat oil to 180°C", "Deep fry chicken until golden brown"};
        Recipe b = new Recipe("Fried Chicken", a, "picture", ingredients, preparation);

        ImageView imageView2 = view.findViewById(R.id.imageView2);
        Bitmap omerProfilePicture = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.omer);
        omerProfilePicture = ImageHelper.getRoundedCornerBitmap(omerProfilePicture,omerProfilePicture.getHeight()/2);
        imageView2.setImageBitmap(omerProfilePicture);

        //SquareImageView imageView3 = view.findViewById(R.id.imageView3);
        ImageView imageView3 = view.findViewById(R.id.imageView3);
        Bitmap chickenBitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.chicken);
        chickenBitmap = ImageHelper.getRoundedCornerBitmap(chickenBitmap,chickenBitmap.getHeight()/35);
        imageView3.setImageBitmap(chickenBitmap);

        TextView Owner = view.findViewById(R.id.Owner);
        Owner.setText(a.getFullName());

        TextView recipe = view.findViewById(R.id.recipe);
        recipe.setText(b.getName());

        TextView recipeDirections = view.findViewById(R.id.recipeDirections);
        recipeDirections.setText(b.toString());

        return view;
    }

   /* // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}