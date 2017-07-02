package com.alberto.videoteca;

import android.icu.text.DisplayContext;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 02/07/2017.
 */

public class FragmentPrincipal extends BrowseFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        leerDatos();
        iniciarInterfazUsuario();
        cargarListas();
    }

    private void leerDatos() {
        MovieList.list = new ArrayList<Movie>();
        String json = Utils.loadJSONFromResource( getActivity(),
                R.raw.movies );
        Gson gson = new Gson();
        Type collection = new TypeToken<ArrayList<Movie>>(){}.getType();
        MovieList.list = gson.fromJson( json, collection );
    }

    private void iniciarInterfazUsuario() {
        setTitle( "Videoteca" );
        setHeadersState( HEADERS_ENABLED );
        setHeadersTransitionOnBackEnabled( true );
        setBrandColor( getResources().getColor( R.color.fastlane_background ) );
    }

    private void cargarListas() {
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter() );
        CardPresenter cardPresenter = new CardPresenter();
        List<String> categories = getCategories();
        if( categories == null || categories.isEmpty() ) return;
        for( String category : categories ) {
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(
                    cardPresenter );
            for( Movie movie : MovieList.list ) {
                if( category.equalsIgnoreCase( movie.getCategory()) )
                    listRowAdapter.add( movie );
            }
            if( listRowAdapter.size() > 0 ) {
                HeaderItem header = new HeaderItem(rowsAdapter.size()-1, category);
                rowsAdapter.add( new ListRow( header, listRowAdapter ) );
            }
        }
        setAdapter( rowsAdapter );
    }
    private List<String> getCategories() {
        if( MovieList.list == null )
        return null;
        List<String> categories = new ArrayList<String>();
        for( Movie movie : MovieList.list ) {
            if( !categories.contains( movie.getCategory() ) ) {
                categories.add( movie.getCategory() );
            }
        }
        return categories;
    }
}
