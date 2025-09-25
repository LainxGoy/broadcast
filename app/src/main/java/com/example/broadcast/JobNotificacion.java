package com.example.broadcast;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobNotificacion extends JobService {


    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public boolean onStartJob(final JobParameters params) {
        executorService.execute(() -> {
            try {

                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e("JobNotificacion", "Thread interrupted", e);
            }


            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                Toast.makeText(getApplicationContext(), "Job finalizado", Toast.LENGTH_SHORT).show();
            });


            jobFinished(params, false);
        });


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
