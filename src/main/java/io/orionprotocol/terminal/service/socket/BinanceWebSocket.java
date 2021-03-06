package io.orionprotocol.terminal.service.socket;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.market.OrderBookEntry;
import io.orionprotocol.terminal.repository.db.PairConfigRepository;
import io.orionprotocol.terminal.repository.redis.InMemoryRepository;
import io.orionprotocol.terminal.service.handler.BinanceUpdateHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.orionprotocol.terminal.model.DataType;
import io.orionprotocol.terminal.model.Exchange;
import io.orionprotocol.terminal.model.Pair;
import io.orionprotocol.terminal.model.PairConfig;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class BinanceWebSocket {

    private static final String EXCHANGE_NAME = "BINANCE";

    final static Logger logger = Logger.getLogger(BinanceWebSocket.class);

    @Autowired
    private BinanceUpdateHandler binanceUpdateHandler;

    @Autowired
    private InMemoryRepository inMemoryRepository;

    @Autowired
    private PairConfigRepository pairConfigRepository;

    private static final String API = "https://www.binance.com/api/v1/depth?symbol={PAIR}&limit=1000";

    @PostConstruct
    public void initConncetions() throws IOException {

        PairConfig binanceConfig = pairConfigRepository.findByExchange(EXCHANGE_NAME);

        for (Pair pair : binanceConfig.getPair()) {

            inMemoryRepository.saveLoadSnapshotBinance(pair.getGeneralName(), "0");
            logger.info(String.format("INIT %S PAIR", pair.getCodeName()));

            BinanceApiWebSocketClient client = BinanceApiClientFactory.newInstance().newWebSocketClient();

            client.onDepthEvent(pair.getCodeName().toLowerCase(), depthEvent -> {
                logger.info("New event " + pair.getGeneralName());
                try {

                    String loadSnapshot = inMemoryRepository.getLoadSnapshotBinance(pair.getGeneralName());
                    if (loadSnapshot == null || loadSnapshot.equals("0")) {
                        String ethBtcJson = null;
                        try {
                            ethBtcJson = getFirstSnapshot(API.replace("{PAIR}", pair.getCodeName()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        for (OrderBookEntry orderBook : depthEvent.getAsks()) {
                            binanceUpdateHandler.handleAskPair(Double.valueOf(orderBook.getPrice()), Double.valueOf(orderBook.getQty()), pair.getGeneralName());
                            inMemoryRepository.saveChanges(Exchange.BINANCE, DataType.ASKS, pair.getGeneralName(), orderBook.getPrice());
                        }
                        for (OrderBookEntry orderBook : depthEvent.getBids()) {
                            binanceUpdateHandler.handleBidsPair(Double.valueOf(orderBook.getPrice()), Double.valueOf(orderBook.getQty()), pair.getGeneralName());
                            inMemoryRepository.saveChanges(Exchange.BINANCE, DataType.BIDS, pair.getGeneralName(), orderBook.getPrice());
                        }
                        binanceUpdateHandler.handleFirstSnapshot(ethBtcJson, pair.getGeneralName());
                    }
                    if (loadSnapshot != null && loadSnapshot.equals("1")) {
                        Long lastUpdateId = Long.valueOf(inMemoryRepository.getLastUpdateIdBinance(pair.getGeneralName()));
                        if (depthEvent.getFinalUpdateId() > lastUpdateId) {
                            for (OrderBookEntry orderBook : depthEvent.getAsks()) {
                                binanceUpdateHandler.handleAskPair(Double.valueOf(orderBook.getPrice()), Double.valueOf(orderBook.getQty()), pair.getGeneralName());
                                inMemoryRepository.saveChanges(Exchange.BINANCE, DataType.ASKS, pair.getGeneralName(), orderBook.getPrice());
                            }
                            for (OrderBookEntry orderBook : depthEvent.getBids()) {
                                binanceUpdateHandler.handleBidsPair(Double.valueOf(orderBook.getPrice()), Double.valueOf(orderBook.getQty()), pair.getGeneralName());
                                inMemoryRepository.saveChanges(Exchange.BINANCE, DataType.BIDS, pair.getGeneralName(), orderBook.getPrice());
                            }
                        }
                    }
                }catch (Exception e){
                    logger.error(e);
                }

            });
        }
    }

    public String getFirstSnapshot(String url) throws IOException {

        HttpGet httpGet = new HttpGet(url);

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(httpGet);
        String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        return json;
    }

}
