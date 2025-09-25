package com.example.broadcast;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BroadcastDinamicoActivity extends AppCompatActivity {
    private static final int JOB_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private final BroadcastReceiver airplaneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);
            String mensaje = isAirplaneModeOn ? "Modo avión activado" : "Modo avión desactivado";
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();

            JobInfo jobInfo = getJobInfo(BroadcastDinamicoActivity.this);
            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

            if (jobScheduler != null) {
                jobScheduler.cancel(JOB_ID); // Cancelar cualquier job anterior
                int result = jobScheduler.schedule(jobInfo);

                if (result == JobScheduler.RESULT_SUCCESS) {
                    Toast.makeText(context, "Job programado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Fallo al programar el job", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private static JobInfo getJobInfo(BroadcastDinamicoActivity activity) {
        ComponentName componentName = new ComponentName(activity, JobNotificacion.class);
        return new JobInfo.Builder(JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(false)
                .setOverrideDeadline(5000)   // máximo 5 segundos
                .setMinimumLatency(2000)     // al menos 2 segundos de retraso
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ✅ Crear el IntentFilter y registrarlo
        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(airplaneReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(airplaneReceiver);
    }
}
