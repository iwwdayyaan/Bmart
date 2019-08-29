package com.bart.api.interfaces;


import com.bart.api.ApplicationModule;
import com.bart.api.HttpModule;
import com.bart.fragments.ForgetPasswordFragment;
import com.bart.fragments.HistoryFragment;
import com.bart.fragments.HomeFragment;
import com.bart.fragments.MyArtFragment;
import com.bart.fragments.ProfileFragment;
import com.bart.fragments.RegisterArtFragment;
import com.bart.fragments.RegisterFragment;
import com.bart.fragments.SettingsFragment;
import com.bart.fragments.SigninFragment;
import com.bart.fragments.TransferWalletFragment;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {HttpModule.class, ApplicationModule.class})
public interface AppComponent {
  void inject(RegisterFragment registerFragment);
  void inject(SigninFragment signinFragment);
  void inject(RegisterArtFragment registerArtFragment);
  void inject(HomeFragment homeFragment);

  void inject(HistoryFragment historyFragment);
  void inject(SettingsFragment settingsFragment);
  void inject(ProfileFragment profileFragment);
  void inject(TransferWalletFragment transferWalletFragment);

  void inject(MyArtFragment myArtFragment);
 void inject(ForgetPasswordFragment forgetPasswordFragment);
/*   void inject(LastJobDetailFragment lastJobDetailFragment);

   void inject(CompanyMemManageJobFragment companyMemManageJobFragment);
   void inject(CompanyMemPostJobFragment companyMemPostJobFragment);
   void inject(JobPortalFragment jobPortalFragment);
   void inject(NoOfCandidatesFragment noOfCandidatesFragment);*/
}
