package asynctask;

import android.os.AsyncTask;
import android.os.Bundle;

import exception.CowboyException;


public abstract class BasicAsyncTask<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {

	protected Bundle doException(Exception e, String asyncTaskTag) {

		Bundle bundle = new Bundle();

		bundle.putString("doException", ((CowboyException) e).getMessage());

		return bundle;
	}

}
