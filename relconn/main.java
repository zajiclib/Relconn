import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/**
 *
 * @author Libor Zajíček
 */
public final class main {

    public final Vrchol[] vrcholy;

    public static Scanner sc = new Scanner(System.in);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int n = sc.nextInt();
        int m = sc.nextInt();

        new main(n, m);
    }

    public main(int n, int m) {

        this.vrcholy = new Vrchol[n];

        // vytvoreni vrcholu s indexem i
        for (int i = 0; i < vrcholy.length; i++) {
            vrcholy[i] = new Vrchol(i);
        }

        // vytvoreni hran na danych bodech s danou pravdepodobnosti
        for (int i = 0; i < m; i++) {

            int s = sc.nextInt(); // vychozi bod
            int t = sc.nextInt(); // cilovy bod
            double pravdepodobnost = sc.nextDouble();
            vrcholy[s].pridat(vrcholy[t], pravdepodobnost);
        }

        // N radku s dotazy z bodu s do bodu t
        int N = sc.nextInt();
        for (int i = 0; i < N; i++) {
            dijkstra(sc.nextInt(), sc.nextInt());
        }
    }

    /**
     * Dijkstruv algoritmus "Invertovany" hledame nejvetsi pravdepodbnost, takze
     * nastavujeme ohodnoceni ostatnich vrcholu na 0
     * 
     * pozn. zpetne vazby se nezadavaji, cili se algoritmus ani nemuze vracet 
     * (coz by nekdy mohlo vezt k zacykleni - ve vetsine pripadu)
     * 
     *
     * Ohodnoceni nebude nikdy zaporne
     *
     * @param zVrcholu z bodu
     * @param doVrcholu do bodu
     */
    public void dijkstra(int zVrcholu, int doVrcholu) {

        // prioritni fronta 
        final Queue<Vrchol> fronta = new PriorityQueue<>();
        fronta.add(vrcholy[zVrcholu]);

        // nastaveni vsech uzlu na 0 
        for (Vrchol n : vrcholy) {
            n.ohodnoceni = 0;
            n.skok = null;
        }

        //nastaveni ohodnoceni uzlu z vychoziho bodu (vychozi bod má pravděpodobnost 100%)
        vrcholy[zVrcholu].ohodnoceni = 1;

        Vrchol aktualniUzel = null;
        Vrchol dalsiUzel = null;
        double noveOhodnoceni;

        while (!fronta.isEmpty()) {

            aktualniUzel = fronta.poll(); // vraci vrchol s nejvyšší prioritou

            // hledani nejvyssiho ohodnoceni 
            for (Map.Entry<Integer, Double> hrana : aktualniUzel.hrany.entrySet()) {

                // vypocet noveho ohodnoceni
                noveOhodnoceni = aktualniUzel.ohodnoceni * hrana.getValue();
                dalsiUzel = vrcholy[hrana.getKey()];

                // vymenit za vyzsi hodnotu = pridat do fronty
                if (noveOhodnoceni > dalsiUzel.ohodnoceni) {
                    
                    dalsiUzel.ohodnoceni = noveOhodnoceni;
                    dalsiUzel.skok = aktualniUzel;
                    fronta.add(dalsiUzel);
                }
            }
        }

        vypis(vrcholy[doVrcholu]);
        System.out.println("");
    }

    /**
     * rekurzivni vypisovani = zjisteni z predchudcu
     *
     * @param n
     */
    private void vypis(Vrchol n) {

        if (n.predchudce() != -1) {
            vypis(vrcholy[n.predchudce()]);
        }
        System.out.print(n.index + " ");

    }

}

class Vrchol implements Comparable<Vrchol> {

    public final int index;

    public double ohodnoceni = 0;

    public Map<Integer, Double> hrany = new HashMap<>();

    public Vrchol skok = null;

    public Vrchol(int index) {
        this.index = index;
    }

    //prida hrany k uzlu a to same i u souseda 
    public void pridat(Vrchol soused, Double ohodnoceni) {
        hrany.put(soused.index, ohodnoceni);
        soused.hrany.put(this.index, ohodnoceni);
    }

    /**
     * Zjisti predchudce
     *
     * @return -1 kdyz neexistuje cesta
     */
    public int predchudce() {
        return this.skok == null ? -1 : this.skok.index;
    }

    @Override
    public int compareTo(Vrchol s) {
        return (int) (this.ohodnoceni - s.ohodnoceni);
    }
}
