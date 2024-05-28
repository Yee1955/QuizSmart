package Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.personalizedlearningexperienceapp.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class UpgradeActivity extends AppCompatActivity {
    Button PurchaseBTN1, PurchaseBTN2, PurchaseBTN3;
    public static final String clientKey = "ATcbC8Ofy0-dGX0r9YoXs41P-Svvm_3qSFXMZput0cAtqyQTnH9mTyhq6RQrDLtakHag-kNhtQxuJ2tc";
    public static final int PAYPAL_REQUEST_CODE = 123;

    // Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready,
            // switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            // on below line we are passing a client id.
            .clientId(clientKey);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);
        initializeView();

        // Setup button
        PurchaseBTN1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPayment();
            }
        });
        PurchaseBTN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPayment();
            }
        });
        PurchaseBTN3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPayment();
            }
        });
    }

    private void initializeView() {
        PurchaseBTN1 = findViewById(R.id.PurchaseButton1);
        PurchaseBTN2 = findViewById(R.id.PurchaseButton2);
        PurchaseBTN3 = findViewById(R.id.PurchaseButton3);
    }

    private void getPayment() {
        // Getting the amount from editText
        String amount = "1.99";

        try {
            PayPalPayment payment = new PayPalPayment(new BigDecimal(amount), "USD", "Course Fees",
                    PayPalPayment.PAYMENT_INTENT_SALE);

            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

            startActivityForResult(intent, PAYPAL_REQUEST_CODE);
            Log.d("PAYPAL", "Payment intent sent successfully.");
        } catch (Exception e) {
            Log.e("PAYPAL_ERROR", "Failed to send payment intent: " + e.getMessage());
        }
    }

}