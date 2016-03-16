package it.uniba.gruppo10.avi2016.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;

import it.uniba.gruppo10.avi2016.R;
import it.uniba.gruppo10.avi2016.adapters.DrawerAdapter;
import it.uniba.gruppo10.avi2016.fragments.AboutFragment;
import it.uniba.gruppo10.avi2016.fragments.CommitteesFragment;
import it.uniba.gruppo10.avi2016.fragments.EventiFragment;
import it.uniba.gruppo10.avi2016.fragments.InfoPersonaliFragment;
import it.uniba.gruppo10.avi2016.fragments.VenueFragment;

/**
 * In questa activity si ha accesso a tutte le sezioni dell'applicazione tramite il navigation
 * drawer, che istanzierà un fragment diverso per ogni voce. La tecnica è quella dell'UI dinamica.
 */
public class MenuPrincipaleActivity extends Activity {

    private static final String DAY_1_EVENT = "Primo giorno";
    private static final String DAY_2_EVENT = "Secondo giorno";
    private static final String DAY_3_EVENT = "Terzo giorno";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private Fragment eventi;
    private Bundle dati;
    private AdapterView.OnItemClickListener drawerListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    eventi = new EventiFragment();
                    dati = new Bundle();
                    dati.putString("ID", DAY_1_EVENT);
                    eventi.setArguments(dati);
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                    getFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, eventi).commit();

                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    break;
                case 1:
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                    eventi = new EventiFragment();
                    dati = new Bundle();
                    dati.putString("ID", DAY_2_EVENT);
                    eventi.setArguments(dati);
                    getFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, eventi).commit();
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    break;
                case 2:
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();

                    eventi = new EventiFragment();
                    dati = new Bundle();
                    dati.putString("ID", DAY_3_EVENT);
                    eventi.setArguments(dati);
                    getFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, eventi).commit();
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    break;
                case 4:

                    Intent chat = new Intent(getApplicationContext(), ChatActivity.class);
                    startActivity(chat);

                    break;
                case  3:
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();

                    eventi = new EventiFragment();
                    dati = new Bundle();
                    dati.putString("ID","Eventi salvati");
                    eventi.setArguments(dati);
                    getFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, eventi).commit();
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    break;
                case 5:
                    InfoPersonaliFragment info=new InfoPersonaliFragment();
                    dati = new Bundle();
                    dati.putString("ID","Informazioni personali");
                    info.setArguments(dati);
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                    getFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, info).commit();
                    mDrawerLayout.closeDrawer(Gravity.LEFT);

                    break;
                case 6:
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                    getFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, new VenueFragment()).commit();
                    mDrawerLayout.closeDrawer(Gravity.LEFT);

                    break;
                case 7:
                    CommitteesFragment committees=new CommitteesFragment();
                    dati = new Bundle();
                    dati.putString("ID","Comitati");
                    committees.setArguments(dati);
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                    getFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, committees).commit();
                    mDrawerLayout.closeDrawer(Gravity.LEFT);

                    break;
                case 8:
                    CommitteesFragment contacs=new CommitteesFragment();
                    dati = new Bundle();
                    dati.putString("ID","Contatti");
                    contacs.setArguments(dati);
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                    getFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, contacs).commit();
                    mDrawerLayout.closeDrawer(Gravity.LEFT);

                    break;
                case 9:
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                    getFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, new AboutFragment()).commit();
                    mDrawerLayout.closeDrawer(Gravity.LEFT);

                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principale);
        if(savedInstanceState==null){
        prendiRisorse();
        inizializza();}
        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);


        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                drawerArrow, R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        DrawerAdapter adapter = new DrawerAdapter(this);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(drawerListener);
    }

    private void inizializza() {
        eventi = new EventiFragment();
        dati = new Bundle();
        dati.putString("ID", DAY_1_EVENT);
        eventi.setArguments(dati);

        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, eventi).commit();

    }

    private void prendiRisorse() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_principale, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.exit) {
            setResult(Activity.RESULT_OK);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
