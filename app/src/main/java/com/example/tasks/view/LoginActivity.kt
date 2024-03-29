package com.example.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.button_login
import java.util.concurrent.Executor
import kotlinx.android.synthetic.main.activity_login.edit_email
import kotlinx.android.synthetic.main.activity_login.edit_password
import kotlinx.android.synthetic.main.activity_login.text_register

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        setListeners()
        observe()

        mViewModel.isAuthenticationAvailable()
    }

    /*
    ********** MY LOGIN****************
    * email: t100@2.com
    * senha: 123456
    * */

    override fun onClick(v: View) {
        if (v.id == R.id.button_login) {
            handleLogin()
        } else if (v.id == R.id.text_register) {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun showAuthentication() {
        // Executor
        val executor: Executor = ContextCompat.getMainExecutor(this)

        // BiometricPrompt
        val biometricPrompt = BiometricPrompt(this@LoginActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                    super.onAuthenticationSucceeded(result)
                }

            })
        val info: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticação")
            .setSubtitle("Subtitulo")
            .setDescription("Descrição")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(info)
    }

    /**
     * Inicializa os eventos de click
     */
    private fun setListeners() {
        button_login.setOnClickListener(this)
        text_register.setOnClickListener(this)
    }


    /**
     * Observa ViewModel
     */
    private fun observe() {
        mViewModel.login.observe(this, Observer {
            if (it.success()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                val message = it.failure()
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        })
        mViewModel.authentication.observe(this, Observer {
            if (it.isFingerprint) {
                if (it.isLogged) {
                    showAuthentication()
                }
            } else {
                if (it.isLogged) {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
            }
        })
    }

    /**
     * Autentica usuário
     */
    private fun handleLogin() {
        val email = edit_email.text.toString()
        val password = edit_password.text.toString()

        mViewModel.doLogin(email, password)
    }

}
