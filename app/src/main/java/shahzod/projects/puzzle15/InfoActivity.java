package shahzod.projects.puzzle15;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InfoActivity extends AppCompatActivity {
    AppCompatImageButton btnBack2;
    ImageView instaBtn;
    ImageView githubBtn;
    ImageView telegramBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        instaBtn = findViewById(R.id.btn_instagram);
        btnBack2 = findViewById(R.id.orqaga);
        githubBtn = findViewById(R.id.btn_github);
        telegramBtn = findViewById(R.id.btn_telegram);
        btnBack2.setOnClickListener(view -> {
            finish();
        });
        githubBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/shahzoddev777"));
            startActivity(intent);
        });
        telegramBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Shahzodbek_reyimboyev"));
            startActivity(intent);
        });
        instaBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/shahzodbek.r__"));
            startActivity(intent);
        });
    }
}