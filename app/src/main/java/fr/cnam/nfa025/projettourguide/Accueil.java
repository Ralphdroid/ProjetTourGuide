package fr.cnam.nfa025.projettourguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Accueil extends Activity {

    @Override
    public void onCreate (Bundle b) {

        super.onCreate(b);
        this.setContentView(R.layout.accueil_main);

    }

    public void action(View v) {

        switch (v.getId()) {

            case (R.id.creer_compte): {

                Intent i_creation_compte = new Intent(this, CreationCompte.class);
                this.startActivity(i_creation_compte); //, 0);
                break;

            }

            case (R.id.envoyer_login): {

                //on verifie le login et le mot de passe
                String login = ((EditText)findViewById(R.id.login)).getText().toString();
                String pwd = ((EditText)findViewById(R.id.password)).getText().toString();
                monDOA monDOA_user = new monDOA(this.getApplicationContext(), Util.TABLE_USER);

                //C'est ici pour bypasser le login/pwd
                if (monDOA_user.verifCoupleLoginPwd(login, pwd)) {

                    Profile userProfile = monDOA_user.getUserProfile(login, pwd);
                    //Log.i(Util.TRACE, "Accueil: profile du user: " + userProfile.toString());
                    Intent iMenu = new Intent(this, MenuActivity.class);
                    iMenu.putExtra("user_profile", userProfile);
                    this.startActivity(iMenu);

                } else {

                    Toast.makeText(this, "Le login et/ou le mot de passe ne sont pas corerects. Recommencez, s'il vou plaît!", Toast.LENGTH_LONG).show();

                }

                break;
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



    }
}
