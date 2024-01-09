// La momentul rularii Cofetariei, aplicatia va deschide panoul de logare pentru un user deja inregistrat. Daca un client
// se va conecta cu username-ul si parola corect introduse si exista in baza de date, se va deschide aplicatia client. In
// schimb, daca se va conecta cofetarul cu usernameul 'cofetar' si parola 'admin', se va deschide exclusiv pentru acesta
// aplicatia admin, unde acesta are posibilitatea de a adauga, sterge si edita produsele cofetariei.
// Din meniul de logare, se poate face trecerea in meniul de inregistrare foarte rapid apasand butonul 'Inregistreaza-ma'.
// In mod similar, din panoul de inregistrare se poate trece usor in meniul de logare pentru ce userul sa se poata conecta
// cu un cont deja existent apasand butonul 'Conecteaza-ma'
public class Cofetarie {
    public static void main(String[] args) {
        new LoginForm(null);
    }
}
