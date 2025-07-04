package com.curso.ejemplo.activities_fragments_intents;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtén el NavController desde el FragmentContainerView
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container_main);
        NavController navController = navHostFragment.getNavController();

        // Configura el BottomNavigationView con el NavController
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_personas) {
                navController.navigate(R.id.listaPersonaFragment);
                return true;
            } else if (id == R.id.action_carros) {
                //navController.navigate(R.id.listaCarrosFragment);
                return true;
            } else if (id == R.id.action_persona_carro) {
               // navController.navigate(R.id.listaCarroConPersonaFragment);
                return true;
            } else {
                throw new IllegalStateException("Unexpected value: " + id);
            }
        });
    }
}