package br.com.cpmh.plus.marketing;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import br.com.cpmh.plus.R;

public class AddSuspectActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private FirebaseFirestore firestore;
    private FirebaseRemoteConfig config;

    private Suspect suspect;

    private TextView textTypeView;

    private TextInputEditText nameField;
    private TextInputEditText emailField;
    private TextInputEditText phoneField;
    private TextInputEditText birthdayField;
    private TextInputEditText cityField;
    private Spinner federativeUnitField;
    private List<CheckBox> interests;

    private RecyclerView interestsRecyclerView;
    private RecyclerView typeRecyclerView;
    private InterestAdapter interestAdapter;
    private TypeAdapter typeAdapter;

    private Button submitButton;
    private Button cancelButton;
    private RadioButton lastChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_suspect);

        firestore = FirebaseFirestore.getInstance();
        config = FirebaseRemoteConfig.getInstance();

        config.fetch(0).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
                config.activateFetched();
            }
        });

        suspect = new Suspect();

        nameField = findViewById(R.id.name_field);
        textTypeView = findViewById(R.id.textTypeView);
        emailField = findViewById(R.id.email_field);
        phoneField = findViewById(R.id.phone_field);
        cityField = findViewById(R.id.city_field);
        federativeUnitField = findViewById(R.id.federative_unit_field);
        birthdayField = findViewById(R.id.birthday_field);
        typeRecyclerView = findViewById(R.id.type_recycler_view);
        interestsRecyclerView = findViewById(R.id.interests_recycler_view);
        submitButton = findViewById(R.id.submit_button);
        cancelButton = findViewById(R.id.cancel_button);
        interests = new ArrayList<>();

        interestAdapter = new InterestAdapter( Arrays.asList(config.getString("interest_list").split(",")), this);
        typeAdapter = new TypeAdapter( Arrays.asList(config.getString("type_list").split(",")), this);

        typeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        typeRecyclerView.setAdapter(typeAdapter);

        interestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        interestsRecyclerView.setAdapter(interestAdapter);

        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        phoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher("BR"));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.submit_button:
                attemptSubmit();
                break;

            case R.id.cancel_button:
                clear();
                break;
        }
    }
    private void attemptSubmit()
    {
        firestore.collection("suspects").whereEqualTo("email",Objects.requireNonNull(emailField.getText()).toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful())
                {
                    if(task.getResult().isEmpty()) {
                        submit();
                    }
                    else
                    {
                        Toast.makeText(AddSuspectActivity.this, "Cadastro já existente", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void submit()
    {
        submitButton.setEnabled(false);

        suspect.setName(Objects.requireNonNull(nameField.getText()).toString());
        suspect.setEmail(Objects.requireNonNull(emailField.getText()).toString());
        suspect.setPhone(Objects.requireNonNull(phoneField.getText().toString()));
        suspect.setCity(Objects.requireNonNull(cityField.getText().toString()));
        suspect.setFederativeUnit(Objects.requireNonNull(federativeUnitField.getSelectedItem().toString()));
        if(lastChecked !=null)
            suspect.setType(lastChecked.getText().toString());

        for (CheckBox interest:interests)
        {
            suspect.addInterest(interest.getText().toString());
        }

        try
        {
            suspect.setBirthday(DateFormat.getDateInstance(DateFormat.LONG).parse(Objects.requireNonNull(birthdayField.getText()).toString()));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        if(!checkIfNull())
        {
            firestore.collection("suspects").add(suspect);
        }
        clear();
    }

    public boolean checkIfNull()
    {
        String name = suspect.getName();
        String type = suspect.getType();

        boolean abort = false;

        if (name == null || name.isEmpty())
        {
            nameField.setError("Name Required");
            abort = true;
        }
        if (type == null || type.isEmpty())
        {
            textTypeView.setError("Type Required");
            abort = true;
        }
        return abort;
    }

    private void clear()
    {
        submitButton.setEnabled(true);
        suspect = new Suspect();

        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        cityField.setText("");
        birthdayField.setText("");
        federativeUnitField.setSelection(0);

        if(lastChecked!= null)
            lastChecked.setChecked(false);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if(buttonView instanceof RadioButton)
        {
            textTypeView.setError(null);
            if(lastChecked !=null && lastChecked.isChecked())
            {
                lastChecked.setChecked(false);
            }
            lastChecked = (RadioButton) buttonView;
        }
        if(buttonView instanceof CheckBox)
        {
            if(isChecked)
            {
                interests.add((CheckBox)buttonView);
            }
            else
            {
                interests.remove(buttonView);
            }
        }
    }
}
