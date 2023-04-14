package dev.luzifer;

import com.google.gson.Gson;
import dev.luzifer.data.distribution.TaskForce1;
import dev.luzifer.data.match.info.ChampData;
import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.PaladinsBuilder;
import me.skiincraft.api.paladins.entity.match.Match;
import me.skiincraft.api.paladins.entity.match.MatchPlayer;
import me.skiincraft.api.paladins.entity.match.objects.ActiveItem;
import me.skiincraft.api.paladins.entity.match.objects.Ban;
import me.skiincraft.api.paladins.internal.requests.APIRequest;
import me.skiincraft.api.paladins.internal.session.EndPoint;
import me.skiincraft.api.paladins.internal.session.Session;
import me.skiincraft.api.paladins.objects.miscellany.DataUsed;
import me.skiincraft.api.paladins.objects.miscellany.Language;
import me.skiincraft.api.paladins.objects.miscellany.LoadoutItem;
import okhttp3.*;
import org.springframework.util.StopWatch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Client extends JFrame {

    private static long lastMatch = 0;

    public static void main(String[] args) {
        Client client = new Client();
        client.setVisible(true);

        Paladins paladins = new PaladinsBuilder()
                .setAuthKey("ECB79213049D409694D13C45D1BE3AD1")
                .setDevId(4531)
                .build();

        APIRequest<Session> request = paladins.createSession();
        Session session = request.get();
        DataUsed dataUsed = paladins.getDataUsed(session).get();
        client.setRequests("Requests: " + dataUsed.getTotalRequestToday() + "/" + dataUsed.getRequestLimitDaily() + " | " +
                "Sessions: " + dataUsed.getTotalSessionsToday() + "/" + dataUsed.getSessionCap());
        session.setOnValidating(System.out::println);

        EndPoint endPoint = session.getEndPoint();
        final int matchesToFetch = 500000;

        Stack<ChampData> champDataStack = new Stack<>();

        Thread pushThread = new Thread(() -> {
            int totalPushed = 0;
            while (true) {
                if(champDataStack.isEmpty()) continue;
                push(champDataStack.toArray(new ChampData[0]));
                System.out.println("Pushed " + champDataStack.size() + " champs");
                totalPushed += champDataStack.size();
                int[] champIds = new int[champDataStack.size()];
                for(int i = 0; i < champDataStack.size(); i++) {
                    champIds[i] = champDataStack.get(i).getChampId();
                }
                client.log("(" + totalPushed + ") Pushed " + champDataStack.size() + " champs [" + lastMatch + "]");
                champDataStack.clear();

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        pushThread.setDaemon(true);
        pushThread.start();

        long startingMatch = 1218589337;
        long currentMatch = startingMatch;

        List<Long> matchIds = new ArrayList<>();
        for(int i = 0; i < matchesToFetch; i++) {
            matchIds.add(currentMatch);
            currentMatch--;
        }

        List<List<Long>> matchChunks = new ArrayList<>();
        for(int i = 0; i < matchIds.size(); i += 10) {
            matchChunks.add(matchIds.subList(i, Math.min(i + 10, matchIds.size())));
        }

        System.out.println("Match chunks: " + matchChunks.size());

        for(int i = 0; i < matchChunks.size(); i++) {
            int finalI = i;
            TaskForce1.order(() -> matchChunks.get(finalI), list -> {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                for(Match match : endPoint.getMatchDetails(list).get()) {
                    client.updateTotal(matchesToFetch);
                    lastMatch = match.getMatchId();
                    if(!match.isRanked()) continue;
                    for(MatchPlayer matchPlayer : match.getPlayers()) {
                        champDataStack.add(constructChampData(match, matchPlayer));
                    }
                    client.update();
                }
                stopWatch.stop();
            });
        }
    }

    private static final String URL = "http://202.61.202.50:8080/THV6aSBpc3QgZWluIFPDvMOfaQ==/game/post/";
    private static void push(ChampData[] data) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        String jsonData = gson.toJson(data);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // handle success
                System.out.println("POST request successful");
            } else {
                // handle failure
                System.out.println("POST request failed: " + response.code() + " - " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ChampData constructChampData(Match match, MatchPlayer matchPlayer) {

        int averageRank = -1;
        List<Integer> ranks = new ArrayList<>();
        for(MatchPlayer player : match.getPlayers()) {
            int rank = player.getTier().getRankId();
            ranks.add(rank);
        }

        if(ranks.size() > 0) {
            int sum = 0;
            for(int rank : ranks) {
                sum += rank;
            }
            averageRank = sum / ranks.size();
        }

        int categoryId = -1;
        switch (matchPlayer.getChampion(Language.English).get().getRole()) {
            case Damage:
                categoryId = 3;
                break;
            case Flank:
                categoryId = 2;
                break;
            case Support:
                categoryId = 1;
                break;
            case Tank:
                categoryId = 0;
                break;
        }

        return new ChampData(
                (int) match.getMatchId(),
                match.getMapGame(),
                1,
                averageRank,
                getOrMinusOne(match.getBans(), 0),
                getOrMinusOne(match.getBans(), 1),
                getOrMinusOne(match.getBans(), 2),
                getOrMinusOne(match.getBans(), 3),
                getOrMinusOne(match.getBans(), 4),
                getOrMinusOne(match.getBans(), 5),
                match.getTeam1Score(),
                match.getTeam2Score(),
                match.getMatchDuration(),
                match.getMatchDate() == null ? -1 : match.getMatchDate().toInstant().toEpochMilli(),
                6.1,
                (int) matchPlayer.getChampionId(),
                (int) matchPlayer.getActivePlayerId(),
                matchPlayer.getName(),
                matchPlayer.getRegion(),
                matchPlayer.getPlatform().getPortalId()[0],
                matchPlayer.getTier().getRankId(),
                matchPlayer.getTierDetails().getPoints(),
                matchPlayer.getChampionLevel(),
                matchPlayer.hasWon() ? 1 : 0,
                categoryId,
                matchPlayer.getCreditsEarned(),
                (int) matchPlayer.getTalent().getItemId(),
                getOrMinusOneLoadout(matchPlayer.getLoadout(), 0),
                getOrMinusOneLoadout(matchPlayer.getLoadout(), 1),
                getOrMinusOneLoadout(matchPlayer.getLoadout(), 2),
                getOrMinusOneLoadout(matchPlayer.getLoadout(), 3),
                getOrMinusOneLoadout(matchPlayer.getLoadout(), 4),
                getOrMinusOneLoadoutLevel(matchPlayer.getLoadout(), 0),
                getOrMinusOneLoadoutLevel(matchPlayer.getLoadout(), 1),
                getOrMinusOneLoadoutLevel(matchPlayer.getLoadout(), 2),
                getOrMinusOneLoadoutLevel(matchPlayer.getLoadout(), 3),
                getOrMinusOneLoadoutLevel(matchPlayer.getLoadout(), 4),
                getOrMinusOneItem(matchPlayer.getActiveItems().getAsList(), 0),
                getOrMinusOneItem(matchPlayer.getActiveItems().getAsList(), 1),
                getOrMinusOneItem(matchPlayer.getActiveItems().getAsList(), 2),
                getOrMinusOneItem(matchPlayer.getActiveItems().getAsList(), 3),
                getOrMinusOneItemLevel(matchPlayer.getActiveItems().getAsList(), 0),
                getOrMinusOneItemLevel(matchPlayer.getActiveItems().getAsList(), 1),
                getOrMinusOneItemLevel(matchPlayer.getActiveItems().getAsList(), 2),
                getOrMinusOneItemLevel(matchPlayer.getActiveItems().getAsList(), 3),
                matchPlayer.getKillingSpree(),
                matchPlayer.getKills().getKills(),
                matchPlayer.getDeaths(),
                matchPlayer.getAssists(),
                matchPlayer.getDamage().getDamage(),
                matchPlayer.getDamage().getDamageTaken(),
                matchPlayer.getDamage().getDamageMitigated(),
                matchPlayer.getHealing(),
                matchPlayer.getSelfHealing()
        );
    }

    private static int getOrMinusOne(List<Ban> list, int index) {
        if(list == null || list.isEmpty()) return -1;
        if(list.size() > index) {
            if(list.get(index) == null) return -1;
            return (int) list.get(index).getChampionId();
        }
        return -1;
    }

    private static int getOrMinusOneItem(List<ActiveItem> items, int index) {
        if(items == null || items.isEmpty()) return -1;
        if(items.size() > index) {
            if(items.get(index).getItem() == null) return -1;
            return (int) items.get(index).getItem().getItemId();
        }
        return -1;
    }

    private static int getOrMinusOneItemLevel(List<ActiveItem> items, int index) {
        if(items == null || items.isEmpty()) return -1;
        if(items.size() > index) {
            if(items.get(index) == null) return -1;
            return (int) items.get(index).getLevel();
        }
        return -1;
    }

    private static int getOrMinusOneLoadout(List<LoadoutItem> list, int index) {
        if(list == null || list.isEmpty()) return -1;
        if(list.size() > index) {
            if(list.get(index) == null) return -1;
            return (int) list.get(index).getItemId();
        }
        return -1;
    }

    private static int getOrMinusOneLoadoutLevel(List<LoadoutItem> list, int index) {
        if(list == null || list.isEmpty()) return -1;
        if(list.size() > index) {
            if(list.get(index) == null) return -1;
            return (int) list.get(index).getPoints();
        }
        return -1;
    }

    private JTextArea logTextArea;
    private JLabel stateLabel;
    private JLabel requestsLabel;

    private int rankedMatches = 0;
    private int totalMatches = 0;

    public Client() {
        setTitle("Log Book Window");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Last Match was: " + Client.lastMatch);
            }
        });

        // create log text area
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
        JScrollPane scrollPane = new JScrollPane(logTextArea);

        // create state label
        stateLabel = new JLabel("Matches: 0");
        requestsLabel = new JLabel("Requests: 0");

        // add components to the window
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(stateLabel, BorderLayout.SOUTH);
        panel.add(requestsLabel, BorderLayout.NORTH);
        add(panel);
    }

    public void setRequests(String requests) {
        requestsLabel.setText(requests);
    }

    public void log(String message) {
        logTextArea.append(message + "\n");
        logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
    }

    public void update() {
        rankedMatches++;
        stateLabel.setText("Ranked Matches: " + rankedMatches + " | Total Matches: " + totalMatches);
    }

    public void updateTotal(int goal) {
        totalMatches++;
        stateLabel.setText("Ranked Matches: " + rankedMatches + " | Total Matches: " + totalMatches + "/" + goal);
    }
}
