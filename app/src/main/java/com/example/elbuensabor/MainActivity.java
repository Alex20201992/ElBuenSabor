package com.example.elbuensabor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.example.elbuensabor.database.CarritoDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static CarritoDB myDatabase;
    Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                int carritoCant=MainActivity.myDatabase.carritoDao().countCart();
                if(carritoCant==0){
                    Bundle bundle = new Bundle();
                    bundle.putString("carrito_mensaje", "CARRITO DE COMPRAS VACIO");
                    bundle.putString("carrito_detalle", "Actualmente tu carrito de compras se encuentra, Ingresa a la carta de menu y agrega tus productos");
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_carrito_mensaje, bundle);
                }else{
                    Navigation.findNavController(MainActivity.this,R.id.nav_host_fragment).navigate(R.id.nav_listar_carrito);
                }
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard_informativo, R.id.nav_productos, R.id.nav_dashboard_carta,R.id.nav_recepcion_pedidos,R.id.nav_historial_pedidos,R.id.nav_modulo_repartidor)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        myDatabase= Room.databaseBuilder(getApplicationContext(),CarritoDB.class,"Carrito_DB").allowMainThreadQueries().build();

        gestionDeUsuarios(navigationView,navController);
        menu=navigationView.getMenu();
        MenuItem myitem = menu.findItem(R.id.nav_cerrar_sesion);

        myitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return false;
            }
        });

        /*NavInflater navInflater = navController.getNavInflater();
        NavGraph graph = navInflater.inflate(R.navigation.mobile_navigation);

        if (false) {
            graph.setStartDestination(R.id.nav_dashboard_informativo);
        } else {
            graph.setStartDestination(R.id.nav_dashboard_carta );
        }
        navController.setGraph(graph);*/
 }




    public void gestionDeUsuarios(NavigationView navigationView, NavController navController){
        NavInflater navInflater = navController.getNavInflater();
        NavGraph graph = navInflater.inflate(R.navigation.mobile_navigation);

        SharedPreferences preferences=this.getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        String usuariotipo=preferences.getString("nomrol","");
        String email=preferences.getString("email","");
        menu=navigationView.getMenu();
        MenuItem dashboard= menu.findItem(R.id.nav_dashboard_informativo);
        MenuItem recepcionPedidos = menu.findItem(R.id.nav_recepcion_pedidos);
        MenuItem modRepartidor = menu.findItem(R.id.nav_modulo_repartidor);
        MenuItem productos = menu.findItem(R.id.nav_productos);
        MenuItem carta = menu.findItem(R.id.nav_dashboard_carta);
        MenuItem historialPedidos = menu.findItem(R.id.nav_historial_pedidos);
        MenuItem RegistrarEmpleado = menu.findItem(R.id.nav_registrar_empleado);
        FloatingActionButton fab = findViewById(R.id.fab);
        Button btnAgregarCarta = findViewById(R.id.btnAgregarCarta);
        //para el email en el menu lateral
        View headerView = navigationView.getHeaderView(0);
        TextView tvEmailSesion=headerView.findViewById(R.id.tvEmailSesion);
        TextView tvRolSesion=headerView.findViewById(R.id.tvRolSesion);
        tvEmailSesion.setText(email);
        tvRolSesion.setText(usuariotipo);
        //---------------------------------

        if(usuariotipo.equals("ADMINISTRADOR")){
            graph.setStartDestination(R.id.nav_dashboard_informativo);

        }else if(usuariotipo.equals("REPARTIDOR")){
            dashboard.setVisible(false);
            recepcionPedidos.setVisible(false);
            carta.setVisible(false);
            productos.setVisible(false);
            historialPedidos.setVisible(false);
            recepcionPedidos.setVisible(false);
            RegistrarEmpleado.setVisible(false);
            fab.hide();
            //Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_modulo_repartidor);
            graph.setStartDestination(R.id.nav_modulo_repartidor);
        }else if(usuariotipo.equals("CLIENTE")){
            dashboard.setVisible(false);
            recepcionPedidos.setVisible(false);
            modRepartidor.setVisible(false);
            productos.setVisible(false);
            recepcionPedidos.setVisible(false);
            RegistrarEmpleado.setVisible(false);
            //Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_dashboard_carta);
            graph.setStartDestination(R.id.nav_dashboard_carta);
        }
        navController.setGraph(graph);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

   
}