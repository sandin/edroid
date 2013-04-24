package com.edroid.utils;

import android.os.AsyncTask;

public class TaskUtils {

	/**
     * Cancel an {@link AsyncTask}.  If it's already running, it'll be interrupted.
     */
    public static void cancelTaskInterrupt(AsyncTask<?, ?, ?> task) {
        cancelTask(task, true);
    }

    /**
     * Cancel an {@link AsyncTask}.
     *
     * @param mayInterruptIfRunning <tt>true</tt> if the thread executing this
     *        task should be interrupted; otherwise, in-progress tasks are allowed
     *        to complete.
     */
    public static void cancelTask(AsyncTask<?, ?, ?> task, boolean mayInterruptIfRunning) {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(mayInterruptIfRunning);
        }
    }
    
    /**
     * Check the task is running or not.
     * 
     * @param task
     * @return
     */
    public static boolean isRunning(AsyncTask<?, ?, ?> task) {
    	return (task != null && task.getStatus() != AsyncTask.Status.FINISHED);
    }

}
