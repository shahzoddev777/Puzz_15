package shahzod.projects.puzzle15;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


public class HomeActivity extends AppCompatActivity {
    AppCompatButton contino;
    AppCompatButton btnexit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitDialog();
            }
        });

        EditText nameInput = findViewById(R.id.sozlar);
        Button button = findViewById(R.id.startbtn);
        button.setOnClickListener(v -> {
            String username = nameInput.getText().toString().trim();
            if (!username.isEmpty()) {

                getSharedPreferences("PUZZLE_SAVE", MODE_PRIVATE).edit().clear().apply();
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra("uernameMain", username);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Ismingizni kiriting!", Toast.LENGTH_SHORT).show();
            }
        });
        AppCompatButton info = findViewById(R.id.infon);
        info.setOnClickListener(view -> {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        });
        contino = findViewById(R.id.continio);
        btnexit = findViewById(R.id.btn_exit);
        contino.setOnClickListener(view -> {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });
        btnexit.setOnClickListener(view -> showExitDialog());
    }

    private void showExitDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_custom, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView message = view.findViewById(R.id.dialog_message);
        message.setText("Ilovadan chiqishni xohlaysizmi?");

        AppCompatButton btnNo = view.findViewById(R.id.btn_no);
        AppCompatButton btnYes = view.findViewById(R.id.btn_yes);

        btnNo.setOnClickListener(v -> dialog.dismiss());
        btnYes.setOnClickListener(v -> {
            dialog.dismiss();
            finishAffinity();
        });

        dialog.show();
    }

    @Override
    protected void onResume() {
        if (getSharedPreferences("PUZZLE_SAVE", MODE_PRIVATE).getBoolean("saved",false)){
            contino.setVisibility(View.VISIBLE);
        }else {
            contino.setVisibility(View.INVISIBLE);
        }
        super.onResume();
    }
}
