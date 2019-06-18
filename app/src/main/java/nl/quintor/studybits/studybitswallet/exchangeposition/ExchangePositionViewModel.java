package nl.quintor.studybits.studybitswallet.exchangeposition;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import nl.quintor.studybits.indy.wrapper.message.MessageEnvelopeCodec;
import nl.quintor.studybits.indy.wrapper.util.AsyncUtil;
import nl.quintor.studybits.studybitswallet.AgentClient;
import nl.quintor.studybits.studybitswallet.room.entity.University;

public class ExchangePositionViewModel extends AndroidViewModel {
    private MutableLiveData<List<ExchangePosition>> exchangePositions = new MutableLiveData<>();

    public ExchangePositionViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(List<University> universities, MessageEnvelopeCodec codec) {
        List<ExchangePosition> newExchangePositions =  universities
                .stream()
                .map(AsyncUtil.wrapException(university -> new AgentClient(university, codec).getExchangePositions()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());


        exchangePositions.setValue(newExchangePositions);
    }

    public LiveData<List<ExchangePosition>> getExchangePositions() {
        return exchangePositions;
    }
}
