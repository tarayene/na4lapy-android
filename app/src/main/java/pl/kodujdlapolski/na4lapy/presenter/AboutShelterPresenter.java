package pl.kodujdlapolski.na4lapy.presenter;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import javax.inject.Inject;

import pl.kodujdlapolski.na4lapy.Na4LapyApp;
import pl.kodujdlapolski.na4lapy.model.Shelter;
import pl.kodujdlapolski.na4lapy.repository.RepositoryService;
import pl.kodujdlapolski.na4lapy.sync.SynchronizationService;
import pl.kodujdlapolski.na4lapy.sync.receiver.SynchronizationReceiver;
import pl.kodujdlapolski.na4lapy.ui.about_shelter.AboutShelterActivity;
import pl.kodujdlapolski.na4lapy.ui.about_shelter.AboutShelterFragment;

/**
 * Created by Natalia on 2016-03-01.
 */
public class AboutShelterPresenter implements SynchronizationReceiver.SynchronizationReceiverCallback {

    @Inject
    SynchronizationService synchronizationService;

    @Inject
    RepositoryService repositoryService;

    private SynchronizationReceiver synchronizationReceiver;

    private AboutShelterFragment aboutShelterFragment;
    private AboutShelterActivity aboutShelterActivity;
    private final Long shelterId;
    private Shelter shelter;
    private boolean isAfterSynchronization = false;

    public AboutShelterPresenter(AboutShelterFragment aboutShelterFragment) {
        this.aboutShelterFragment = aboutShelterFragment;
        aboutShelterActivity = (AboutShelterActivity) aboutShelterFragment.getActivity();
        shelterId = aboutShelterActivity.getShelterId();
        ((Na4LapyApp) aboutShelterActivity.getApplication()).getComponent().inject(this);
        synchronizationReceiver = new SynchronizationReceiver(this);
        startDownloadingData();
    }

    public void startDownloadingData() {
        aboutShelterFragment.showProgressHideContent(true);
        isAfterSynchronization = false;
        getData();
        synchronizationService.synchronize();
    }

    @Override
    public void onSynchronizationSuccess() {
        if (aboutShelterFragment != null && aboutShelterFragment.isAdded()) {
            isAfterSynchronization = true;
            getData();
        }
    }

    @Override
    public void onSynchronizationFail() {
        if (shelter == null && aboutShelterFragment != null && aboutShelterFragment.isAdded()) {
            aboutShelterFragment.showError();
        }
    }

    public void onActivityStart() {
        LocalBroadcastManager.getInstance(aboutShelterActivity)
                .registerReceiver(synchronizationReceiver, SynchronizationReceiver.getIntentFilter());

    }

    public void onActivityStop() {
        LocalBroadcastManager.getInstance(aboutShelterActivity).unregisterReceiver(synchronizationReceiver);
    }

    private void onShelterAvailable() {
        if (shelter != null) {
            ((AboutShelterActivity) aboutShelterFragment.getActivity()).setShareIntent(getShareIntent());
            aboutShelterFragment.populateView(shelter);
            aboutShelterFragment.showProgressHideContent(false);
        } else {
            if (isAfterSynchronization) {
                aboutShelterFragment.showError();
            } else {
                aboutShelterFragment.showProgressHideContent(true);
            }
        }
    }

    private Intent getShareIntent() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, aboutShelterFragment.getFormattedTitle());
        sharingIntent.putExtra(Intent.EXTRA_TEXT, aboutShelterFragment.getFormattedInfoText());
        return sharingIntent;
    }

    private void getData() {
        repositoryService.getShelter(shelterId).subscribe(s -> {
            shelter = s;
            onShelterAvailable();
        });
    }
}
