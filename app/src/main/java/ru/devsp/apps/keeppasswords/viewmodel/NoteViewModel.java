package ru.devsp.apps.keeppasswords.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import com.google.android.apps.authenticator.Base32String;
import com.google.android.apps.authenticator.PasscodeGenerator;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

import ru.devsp.apps.keeppasswords.model.objects.Field;
import ru.devsp.apps.keeppasswords.model.objects.Note;
import ru.devsp.apps.keeppasswords.repository.FieldsRepository;
import ru.devsp.apps.keeppasswords.repository.NoteRepository;
import ru.devsp.apps.keeppasswords.tools.AbsentLiveData;
import ru.devsp.apps.keeppasswords.tools.Encoder;
import ru.devsp.apps.keeppasswords.tools.Logger;


/**
 * Запись
 * Created by gen on 20.10.2017.
 */

public class NoteViewModel extends ViewModel {

    private NoteRepository mNoteRepository;
    private FieldsRepository mFieldsRepository;
    private LiveData<Note> mNote;
    private LiveData<List<Field>> mFields;
    private MutableLiveData<Long> mNoteSwitcher = new MutableLiveData<>();

    @Inject
    NoteViewModel(NoteRepository noteRepository, FieldsRepository fieldsRepository) {
        mNoteRepository = noteRepository;
        mFieldsRepository = fieldsRepository;

        mNote = Transformations.switchMap(mNoteSwitcher, id -> {
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return mNoteRepository.getNote(id);
            }
        });

        mFields = Transformations.switchMap(mNoteSwitcher, id ->{
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return mFieldsRepository.getFields(id);
            }
        });

    }

    public LiveData<Note> getNote(){
        return mNote;
    }

    public LiveData<List<Field>> getFields(){
        return mFields;
    }

    public void setNote(long id){
        mNoteSwitcher.postValue(id);
    }

    public void update(Note item) {
        mNoteRepository.update(item);
    }

    public void update(List<Field> items){
        mFieldsRepository.update(items);
    }

    public void saveField(Field item){
        mFieldsRepository.add(item);
    }

    public static String getOnePass(Encoder encoder, String key){

        String value = encoder.dec(key);
        if(!TextUtils.isEmpty(value)) {
            PasscodeGenerator.Signer signer = getSigningOracle(value);
            if (signer != null) {
                try {
                    PasscodeGenerator pcg = new PasscodeGenerator(signer, 6);
                    long opt = getValueAtTime(millisToSeconds(System.currentTimeMillis()));
                    return pcg.generateResponseCode(opt);
                } catch (GeneralSecurityException ex) {
                    Logger.e(ex);
                }
            }
        }
        return null;
    }

    private static long millisToSeconds(long timeMillis) {
        return timeMillis / 1000;
    }

    private static PasscodeGenerator.Signer getSigningOracle(String secret) {
        try {
            byte[] keyBytes = decodeKey(secret);
            final Mac mac = Mac.getInstance("HMACSHA1");
            mac.init(new SecretKeySpec(keyBytes, ""));
            return mac::doFinal;
        } catch (Base32String.DecodingException
                | NoSuchAlgorithmException
                | InvalidKeyException ex) {
            Logger.e(ex);
        }
        return null;
    }

    private static byte[] decodeKey(String secret) throws Base32String.DecodingException {
        return Base32String.decode(secret);
    }

    private static long getValueAtTime(long time) {
        if (time >= 0) {
            return time / 30;
        } else {
            return (time - (30 - 1)) / 30;
        }
    }

}
