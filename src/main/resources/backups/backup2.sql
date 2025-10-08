-- MySQL dump 10.13  Distrib 8.0.43, for Linux (x86_64)
--
-- Host: localhost    Database: quizzer
-- ------------------------------------------------------
-- Server version	8.0.43
drop database if exists quizzer;
create database if not exists quizzer;
use quizzer;
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `question_id` int NOT NULL,
  `answer` varchar(2000) DEFAULT NULL,
  `is_correct` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `question_id` (`question_id`),
  CONSTRAINT `answer_ibfk_1` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=651 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer`
--

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;
INSERT INTO `answer` VALUES (6,3,'Lo studio del comportamento deviante',0),(7,3,'The scientific studi of laemaking, lawbrakingand the response to lawbreaking',1),(8,3,'Lo studio delle leggi penali ',0),(9,3,'L\'analisi della mente criminale',0),(10,4,'I criminali nascono predisposti al crimine',0),(11,4,'L\'ambiente determina il comportamento criminale',0),(12,4,'I comportamenti criminali vengono appresi attraverso le interazioni con gli altri',1),(13,4,'La povert  la causa principale del crimine',0),(14,5,'Resistant',0),(15,5,'Removable',1),(16,5,'Readable',0),(17,5,'Reliable',0),(18,6,'I crimini sono sempre distribuiti in modo casuale',0),(19,6,'I crimini sono uniformi nel tempo e nello spazio',0),(20,6,'I crimini non sono ne casuali n uniformi nel tempo e nello spazio',1),(21,6,'I crimini seguono sempre lo stesso schema',0),(22,7,' Tempo, luogo e vittima',0),(23,7,'Target appetibile, assenza di guardiano capace e offender motivato',1),(24,7,'Denaro, movente e opportunit',0),(25,7,'Arma, vittima e luogo',0),(30,9,'Una forma di responsabilit giuridica per le imprese in caso di commissione di reati nel proprio interesse o vantaggio',1),(31,9,'una forma di responsabilit civile',0),(32,9,'Conseguenza di violazione di norme amministrative',0),(33,10,'L\'obbligo di risarcire i danni derivanti dal reato',0),(34,10,'L\'applicazione di una sanzione pecuniaria e nei casi piu gravi una sanzione di carattere interdittivo',1),(35,10,'La nomina di un commissario giudiziale',0),(52,52,'Un complesso di regole, strumenti e condotte funzionalie ad individuare e prevenire le condotte penalmente rilevanti posti in essere dall\'Ente',1),(53,52,'Un elenco di reati presupposto per la responsabilit amministrativa dell\'Ente ex D.lgs. 231/2001',0),(54,52,'Il Codice Etico',0),(55,53,'Viene licenziato in ragione della segnalazione',0),(56,53,'Ha diritto della Tutela della riservatezza della propria identit e al divieto della ritorsione',1),(57,53,'Viene sospeso dalle sue funzioni per il tempo necessario all\'istruttoria',0),(58,54,'solo se la condotta illecita viene realizzata in tutta Italia',0),(59,54,'anche se la condotta illecita non  stata tipizzata in una norma specifica',0),(60,54,'se l\'azione o l\'omissione (che costituiscono il reato) sono avvenuti in tutto e in parte in Italia',1),(61,55,'capitale fisso/fondi',0),(62,55,'impieghi/debiti',0),(63,55,'mezzi propri/mezzi terzi',0),(64,55,'impieghi/fonti',1),(65,55,'investimenti/finanziamenti',0),(66,56,'quello adottato per l\'attualizzazione dei flussi di cassa',0),(67,56,'quello che rende uguale a zero la somma di tutti i flussi di cassa dell\'investimento',1),(68,56,'il tasso di rendimento del capitale investito nel progetto',0),(69,56,'Il tasso di rendimento atteso dagli azionisti per qualsiasi progetto di investimento',0),(70,57,'la somma dei flussi di casssa in entrata in un progetto tudini pre unitarie',0),(85,61,'nella costituzione',0),(86,61,'negli art. 133 e 134 del TULPS',1),(87,62,'Individuare le polizze assicurative pi idonee',0),(88,62,'Conoscere il profilo di rischi da fronteggiare',1),(89,62,'Decidere come gestire le emergenze',0),(90,63,'Ridurre la probabilit di accadimento',1),(91,63,'Ridurre i danni',0),(92,63,'Limitare i costi assicurativi',0),(93,64,'E\' la Formazione sul rischio',0),(94,64,'E\' la capacit di individuare una fonte di pericolo',0),(95,64,' l\'atteggiamento di fronte al pericolo',1),(96,65,'Tutti i rischi esistenti in azienda con particolare attenzione alle condizioni specifiche dei lavoratori interessati',1),(97,65,'Tutti i rischi di natura psicosociale, definiblii in termini di interazioni tra contenuto del lavoro, condizioni ambientali e organizzative ed esigenz/competenze dei lavoratori',0),(98,65,'tutti i rischi esistenti in azienda connessi alla scelta delle attrezzature di lavoro, delle sostanze o dei preparati chimici impiegati, nonch della sistemazione dei luoghi di lavoro',0),(99,66,'3',1),(100,66,'5',0),(101,66,'Dipende',0),(102,67,' spesso rigida e fondata su principi assoluti',0),(103,67,' l\'insieme di valori, norme e convinzioni che una persona o una societ considera giusti o sbagliatati',0),(104,67,'Valuta criticamente la situazione, talvolta anche in contrasto con le regole comuni',1),(105,68,'Penale',0),(106,68,'Civile ',0),(107,68,'Amministrativa',0),(108,68,'Tutte le precedenti',1),(109,69,'quella economica  il primo livello (alla base della piramide)',1),(110,69,'quella economica  all\'ultimo livello (al vertice della piramide)',0),(111,69,'Quella etica non  contemplata',0),(112,70,'Costituisce anche uno strumento di governance e gestione strategica dell\'impresa',1),(113,70,'Pu essere sostituita da una pi semplice carta dei valori',0),(114,70,'E\' previsto dal D.Lgs. 231/2001 solo in casi particolari',0),(115,71,'Evento che si manifesta in azienda e dal quale  possibile guadagnare o perdere',0),(116,71,'Evento colposo che si manifesta in un\'azienda',0),(117,71,'Evento doloso che procura danni diretti, indiretti, consequenziali',0),(118,71,'Eventualit che una minaccia possa manifestarsi e procurare danni',1),(119,72,'Un rischio strategico',0),(120,72,'Un rischio puro',1),(121,72,'Un rischio speculativo',0),(122,73,'In un contesto prettamente religioso indica come concrettizzare i principi dottrinali',0),(123,73,'Studia i fondamenti oggettivi e razionali che permettono di distinguere i comportamenti umani in leciti o inappropriati',1),(124,73,'identifica cio\' che  corretto e lo differenzia da ci che  lecito',0),(125,73,'Fornisce chiavi di lettura per interpretare i comportamenti altrui',0),(126,74,'propriet intrinseca che e potenzialmente puo\' casusare danni',1),(127,74,'la probabilit ch eun certo evento si verifichi e l\'impatto che ne puo\' derivare',0),(128,74,'elemento che possiede il potenziale di originare il rischio',0),(154,103,'Furto',1),(155,103,'Rapina',0),(156,103,'Taccheggio',0),(157,104,'Doppo 90 giorni dall\'atto',0),(158,104,'Entro 3 mesi dal fatto',0),(159,104,'Entro 3  mesi dal giorno della notizia del fatto',1),(160,105,'In nessun modo sono la stessa cosa',0),(161,105,'Perch la rapina frutta un bottino superiore',0),(162,105,'Per l\'elemento della violenza che nel furto manca',1),(163,106,'La libert personale  inviolabile , salvo quanto disposto dai comma 2 e 3',1),(164,106,'La libert personalepuo\' sempre essere violata in caso di necessit',0),(165,106,'la libert personale pu essere sempre violata  dalle Forze dell\'Ordine',0),(166,107,'E\' prerogativa esclusiva delle forze dell\'ordine',0),(167,107,'E\' prerogativa anche dei privati nelle forme e nei casi previsti dall\'art. 383 cpp',1),(168,107,'E\' prerogativa esclusiva della Guardia di Finanza',0),(169,108,'istituzione, formale, polizia',0),(170,108,'istituzione, sociale, ambientale',1),(171,108,'sociale, polizia, tecnologico',0),(172,108,'giudiziario, ambientale, videosorveglianza',0),(173,109,'la sicurezza reale/il livello di criminalit',0),(174,109,'la sicurezza percepita',0),(175,109,'Entrambe',1),(176,109,'Nessuno delle due : i fattori determinanti sono altri',0),(177,110,'Per valutare la sicurezza di un luogo  sufficiente monitorare l\'andamento dei reati',0),(178,110,'No, no  necessario rappresentare l\'interno quadro anche attraverso indagini di vittimizzazione e  sulla percezione',1),(179,110,'Solo nelle zone ad elevata criminalit',0),(180,110,'L\'andamento dei reati non  significativo',0),(181,111,'l\'illuminazione degli spazi',0),(182,111,'gli orari di apertura delle attivit commerciali',0),(183,111,'Nessuno di questi',0),(184,111,'Entrambi',1),(185,112,'La sicurezza reale  sempre slegata da quella percepita',0),(186,112,'Sicurezza reale e percepita hanno origini solo in parte comune e vanno affrontate ciascuna con strumenti appropriati',1),(187,112,'La sicurezza reale  sempre correlata a quella percepita',0),(188,112,'Nessuna delle tre',0),(189,113,'solo prima dell\'edificazione',0),(190,113,'solo nei luoghi gi esistenti',0),(191,113,'mai',0),(192,113,'in entrambi i casi ma con costi di edificazione molt differenti',1),(193,114,'Un cittadino si sente insicuro quando',0),(194,114,'vive in un ambiente squallido e trascurato',0),(195,114,'riceve dai media continue notizie di violenza e allarme',0),(196,114,'tutte le precedenti',1),(197,115,'Vero, sempre',1),(198,115,'Falso,  sufficiente il controllo delle forze dell\'Ordine',0),(199,115,'Dipende dal contesto',0),(200,115,'Dipende dal tipo di problemi identificati',0),(201,116,'migliorano l\'attenzione',0),(202,116,'riducono l\'efficienza cognitiva',1),(203,116,'ci consentono di ricordare meglio le cose negative',0),(204,117,'E\' associato all\'adozione di comportamenti non sicuri',1),(205,117,'ha effetti negativi sulla salute ma non influenza i comportamenti',0),(206,117,'nel lavoro, aumenta la preoccupazione di commettere errori e quindi ci spinge ad essere piu attenti e prudenti a cosa facciamo',0),(207,118,'attiva il sistema nervoso autonomo predisponendo l\'organismo ad una risposta di attacco  fuga',0),(208,118,'puo\' divenire una risposta patologica se prodotta in modo intenso e/o per lunghi periodi di tempo',0),(209,118,'nasce come una risposta fisiologica ed adattiva',0),(210,118,'Tutte le precedenti',1),(211,119,'E\' utile solo se si adottano strategie passive (es. dormire)',0),(212,119,'non  necessario per chi  abituato a lavorare molto e con costanza',0),(213,119,' fondamentale per consentire ai singoli sistemi funzionali di tornare a livelli di attivazione ottimale',1),(214,120,'Primaria',1),(215,120,'secondaria',0),(216,120,'Terziaria',0),(217,121,'ai tecnici che devono  garantire il corretto funzionamento di macchine, attrezzature impianti di sicurezza, nonch ambienti di lavoro salubri',0),(218,121,'all\'imprenditore o al titolare delle attivit o al rappresentante legale, a seconda della dimensione aziendale, in quanto destinatari delle sanzioni',0),(219,121,'A tutti i soggetti che compongono la struttura organizzativa finalizzata alla produzione di beni e servizi',1),(220,122,'Valutare situazioni per conformarsi alle norme di igiene del lavoro',0),(221,122,'Valutare situazioni per comformarsi alle norme di igiene del lavoro',0),(222,122,'Eliminare i rischi e/o ridurre al minimo esposizione del lavoratore',1),(223,123,'prima di modifiche impiantistiche ai processi produttivi',0),(224,123,'solo per modifiche significative',0),(225,123,'prima di modifiche di natura tecnica, organizzativa e gestionale',1),(226,124,'insieme di consulenti interni o esterni che si occupano di controllare macchine, impianti e attrezzature',0),(227,124,'struttura individuata dal datore di lavoro, per coordinare e sviluppare le attivit di prevenzione, a supporto della struttura aziendale',1),(228,124,'struttura che si occupa di sicurezza per tutta l\'azienda , in modo autonomo',0),(229,125,'al Datore di lavoro e al dirigente',0),(230,125,'al datore di lavoro al dirigente e al preposto nell\'ambito delle proprie attribuzioni',1),(231,125,'al datore di lavoro',0),(232,126,'partecipano al processo di valutazione del rischio e dell\'individuazione delle misure di prevenzione e protezione',1),(233,126,'verificano esclusivamente il rispetto di misure da parte dei lavoratori',0),(234,126,'segnalazioni solo situazioni di non conformit perch altri li gestiscano',0),(235,127,'estremamente consigliate , ma facotative',0),(236,127,'una scelta del datore di lavoro',0),(237,127,'un obbligo di legge',1),(238,128,'Cooperazione e coordinamento tra i datori di lavoro compreso i subappaltatori',1),(239,128,'Redazione del contratto quadro con le imprese coinvolte, con gli aspetti di sicurezza',0),(240,128,'Procedura dettagliata affinch ognuno operi autonomamente',0),(241,129,'Non  obbligatoria per gli obiettivi del D.lgs. 81/08',0),(242,129,'E\' obbligatoria per il funzionamento di macchine e impianti al Titolo III del D.lgs 81/08',0),(243,129,'E\' obbligatoria e sanzionata penalmente in tutti gli art. del D.Lgs 81/08 ove richiamata',1),(244,130,'Una forma di responsabilit giuridica per le imprese in caso di commissione di reati nel proprio interesse o vantaggio',1),(245,130,'Una forma di responsabilit civile',0),(246,130,'Conseguenza di violazione di norme amministrative',0),(247,131,'capitale fisso/fonti',0),(248,131,'impieghi/debiti',0),(249,131,'mezzi propri/ mezzi terzi',0),(250,131,'impieghi/fonti',1),(251,131,'investimenti/finanziamenti',0),(252,132,'E\' spesso rigida fondata su principi assoluti',0),(253,132,'E\' l\'insieme di valori norme e convinzioni che una persona o una societ considera giusti o sbagliati',0),(254,132,'Valuta criticamente la situazione, talvolta anche in contrasto con le regole comuni',1),(255,133,'Gestibili efficacemente dall\'organizzazione',0),(256,133,'Rischi che non  possibile influenzare ma non gestire totalmente',0),(257,133,'rischi da cui si  totalmente dipendenti',1),(258,134,'Una parte interessata interna',1),(259,134,'Una parte interessata esterna',0),(260,134,'Non  una parte interessata perch non esistono obblighi contrattuali',0),(261,135,'Governance e cultura',0),(262,135,'Aspetti macroeconomici',1),(263,135,'Brand',0),(264,136,'Il processo di valutazione del rischio tiene conto dei fattori umani e culturali',0),(265,136,'Il processo di gestione dei rischi  parte integrante di tutti i processi organizzativi',0),(266,136,'Il processo di gestione del rischio  standardizzato per tutte le organizzazioni',1),(267,137,'Costi diretti + Costi indiretti + Costi di prevenzione + Costi di protezione',0),(268,137,'Solo i costi diretti e inderetti',0),(269,137,'Costi diretti + Costi indiretti + Costi di prevenzione + Costi di protezione + Costi di trasferimento',1),(270,138,'Enviromental Risk management',0),(271,138,'Enterprise Routine Management',0),(272,138,'Enterprise risk management',1),(273,139,'Comunicazione e Consultazione, Analisi del Contesto e Risk Treatment',0),(274,139,'Comunicazione e Consultazione, Analisi del Contesto, Risk assessment e Risk Treatment, Monitoraggio e Revisione',1),(275,139,'Risk assessment e Risk Treatment, Monitoraggio',0),(276,140,'Interno ed Esterno',1),(277,140,'Internazionale e Nazionale',0),(278,140,'Nessuna delle precedenti',0),(279,141,'Probabilit * Vulnerabilit',0),(280,141,'Probabilit * Impatto',1),(281,141,'Vulnerabilit *Frequenza',0),(282,142,'Puri e speculativi',0),(283,142,'Esterni e interni',0),(284,142,'Strategici e operativi',0),(285,142,'Sistematici e specifici',0),(286,142,'Cambio e liquidit',0),(287,142,'Solo le prime 4 Risposte',1),(288,143,'Grado di applicazione delle contromisure, grado di efficienza delle tecnologie, grado di adeguateza delle risorse umane',0),(289,143,'Probabilit e gravit',0),(290,143,'Vulnerabilit e frequenza',1),(291,144,'Anelli deboli dell\'organizzazione',0),(292,144,'Grado di applicazione delle procedure, grado di efficienza delle tecnologie, grado di adeguatezza delle risorse umane ',1),(293,144,'Formazione delle persone',0),(294,145,'Individuare le polizze assicurative pi idonee',0),(295,145,'Conoscere il profilo di rischi da fronteggiare',1),(296,145,'Decidere come gestire le emergneze',0),(297,146,'Limitare la probabilit di accadimento',0),(298,146,'Prevenire un \'ulteriore sanzione legale',0),(299,146,'Ridurre i tempi di intervento e quindi i danni',1),(300,147,'Ridurre la probabilit di accadimento',1),(301,147,'Ridurre i danni',0),(302,147,'Limitare i costi assicurativi',0),(303,148,'Finanziario - assicurativo',0),(304,148,'Tecnico - contrattuale',0),(305,148,'Entrambe le risposte',1),(306,149,'Statico e immutabile nel tempo',0),(307,149,'Circolare (PDCA plan do check act) e adattivo',1),(308,149,'Rigorosamente analitico e senza approssimazioni di sorta',0),(309,150,'Definizioni di policy e procedure',0),(310,150,'Definizione della struttura organizzativa',0),(311,150,'Definizione dei piani di formazione e sensibilizzazione',0),(312,150,'Tutte le risposte precedenti',1),(313,151,'Creare nuovo lavoro per gi enti aziendali gi esistenti',0),(314,151,'Creare nuovi enti per lo svolgimento di attivit gi proprie dell\'azienda',0),(315,151,'Trarre ulteriore valore da attivit che gi sono proprie dell\'azienda',1),(316,152,'E\' uno strumento di supporto alla gestione dei progetti',1),(317,152,'E\' uno strumento di data Analysis',0),(318,152,'E\' uno strumento utilizzato per gli audit si security',0),(319,153,'L\'uso \"dell\'arma economica\" nella competizione tra paesi',0),(320,153,'L\'uso dell\'energia per fare pressioni su altri sistemi economici',0),(321,153,'La salvaguardia del \"sistema economico2 interno',0),(322,153,'Tutte e tre le precedenti',1),(323,154,'Le attivit di monitoraggio degli eventi',0),(324,154,'Le attivit che riguardano il Travel Risk Management',0),(325,154,'Le attivit di pianificazione e monitoraggio ma anche quelle di mitigazione',1),(326,154,'Le attivit a supporto della pianificazione strategica',0),(327,155,'Riducendo il tasso di rischiosit e aumentando e stabilizzando i flussi di reddito',1),(328,155,'Aumentando i flussi di reddito',0),(329,155,'Riducendo il tasso di rischiosit del settore e  dell\'azienda',0),(330,155,'Abbassando le spese di ripristino dopo un incidente',0),(331,156,'Sicurezza in azienda, sicurezza nei cantieri e dei sistemi informativi',0),(332,156,'Sicurezza del lavoro, sicurezza delle cose, sicurezza delle persone',0),(333,156,'Sicurezza organizzativa, sicurezza fisica e sicurezza logica',1),(334,156,'Sicurezza delle azioni, sicurezza delle dei pensieri e sicurezza delle intezioni',0),(335,157,'Descrive le strategie e la pianificazione della security',0),(336,157,'Descrive le linee generali e degli obiettivi e le responsabilit delle risorse',1),(337,157,'Indica chi deve fare che cosa su ogni aspetto della security',0),(338,158,'Riservatezza, immediatezza, Disponibilit',0),(339,158,'Riservatezza, Integrit, Disponibilit',1),(340,158,'Identificazione, Autorizzazione, Autenticazione',0),(341,158,'Correttezza, identificabilit, Tracciabilit',0),(342,159,'Controllo accessi e crittografia',0),(343,159,'Antivirus e Backup',0),(344,159,'Sistemi di monitoraggio (IDS, IPS) firewall',0),(345,159,'Sicurezza fisica',0),(346,159,'Tutte le risposte',1),(347,159,'Solo a, b, c',0),(348,160,'Un rischio strategico',0),(349,160,'Un rischio puro',1),(350,160,'Un rischio speculativo',0),(351,161,'protezione, trasferimento, prevenzione, ritenzione, accettazione (Risk appetite)',1),(352,161,'protezione, trasferimento, prevenzione,',0),(353,162,'Il principio della prudenza',0),(354,162,'Il principio della competenza',1),(355,162,'Il principio della Inerenza',0),(356,163,'servizi di sicurezza passiva, effettuati esclusivamente da operatori disarmati;',1),(357,163,'servizi di sicurezza passiva, effettuati esclusivamente da operatori armati;',0),(358,163,'servizi di sicurezza attiva, effettuati esclusivamente da operatori armati;',0),(359,163,'servizi di sicurezza attiva, effettuati principalmente da operatori disarmati.',0),(364,165,'un pubblico ufficiale',0),(365,165,'un incaricato di pubblico servizio',1),(366,165,'un privato cittadino professionalmente formato alla prevenzione di reati contro il patrimonio',0),(367,165,'un agente di polizia giudiziaria',0),(368,166,'dal sindaco della citta dove ha sede legale livp',0),(369,166,'dal questore della citta dove ha sede legale livp',0),(370,166,'dal prefetto del capoluogo di provincia dove ha sede legale livp',1),(371,166,'dal prefetto del capoluogo di provincia di residenza del titolare di detta licenza',0),(376,168,'valore aggiunto, posizione finanziaria netta, costi fissi/variabili',0),(377,168,'costi fissi/variabili, costo del venduto, valore aggiunto',1),(378,168,'costo del venduto, break-even point, costi fissi/variabili',0),(379,168,'valore aggiunto, valore della produzione, margine di contribuzione',0),(380,168,'costo del venduto, costi fissi/variabili, costo del capitale',0),(381,169,'misurare la variazione dellutile netto al variare dei costi fissi',0),(382,169,'valutare la convenienza tra indebitarsi e aumentare il capitale dei soci',0),(383,169,'calcolare il fatturato di pareggio',0),(384,169,'quantificare immediatamente la variazione del reddito operativo al variare del fatturato',1),(385,169,'scegliere se  meglio aumentare i costi fissi o i costi variabili',0),(386,170,'capitale fisso/fonti',0),(387,170,'impieghi/debiti',0),(388,170,'mezzi propri/mezzi di terzi',0),(389,170,'impieghi/fonti',1),(390,171,'sono tassate nellesercizio in cui sono acquistate',0),(391,171,'sono detraibili per intero nellanno di acquisto',0),(392,171,'sono investimenti ad utilit pluriennale',1),(393,171,'non si ammortizzano se non in casi particolari',0),(394,171,'finiscono nello stato patrimoniale del bilancio, alla voce mezzi propri',0),(399,173,'quello adottato per lattualizzazione dei flussi di cassa',0),(400,173,'quello che rende uguale a zero la somma di tutti i flussi di cassa dellinvestimento',1),(401,173,'il tasso di rendimento del capitale investito nel progetto',0),(402,173,'il tasso di rendimento atteso dagli azionisti per qualsiasi progetto di investimento',0),(403,174,' la somma dei flussi di cassa in entrata del progetto',0),(404,174,'la somma dei flussi di cassa positivi del progetto, al netto dellinvestimento iniziale',0),(405,174,'il valore attuale della somma di tutti i flussi di cassa del progetto',1),(406,174,'il valore dei flussi che rende uguale a zero il tasso di rendimento dellinvestimento',0),(407,174,'un altro modo di esprimere il roi dellinvestimento.',0),(419,179,'perch i soggetti privati non sarebbero mai in grado di gestire tali ambiti',0),(420,179,'perch in uno stato democratico  giusto cos',0),(427,182,'sono sempre consentiti a discrezione dellautorit di ps',0),(428,182,'sono consentiti per atto motivato dallautorit giudiziaria o, in casi di eccezionale gravit ed urgenza, anche in via provvisoria dallautorit di ps',1),(429,182,'non sono mai consentiti',0),(430,183,'dal ministro dell\'interno al quale l\'art. 1 della legge 1 aprile 1981, n. 121 ha attribuito la responsabilit della tutela dell\'ordine e della sicurezza pubblica',1),(431,183,'dal presidente della repubblica',0),(432,183,'dal ministro degli esteri',0),(439,186,'nelle consuetudini pre-unitarie',0),(440,186,'nella costituzione',0),(441,186,'negli artt. 133 e 134 del tulps',1),(442,187,'a chiunque si trovi a prestare soccorso ad altri',0),(443,187,'a chiunque porti unarma',0),(444,187,'a quei soggetti che, pubblici dipendenti o semplici privati, possono e debbono - quale che sia la loro posizione soggettiva - formare e manifestare, nell\'ambito di una potest regolata dal diritto pubblico, la volont della p.a., ovvero esercitare, indipendentemente da formali investiture, poteri autoritativi, deliberativi o certificativi, disgiuntamente e non cumulativamente considerati',1),(445,188,'perch i soggetti privati non sarebbero mai in grado di gestire tali ambiti',0),(446,188,'perch in uno stato democratico  giusto cos',0),(447,188,'perch l\'art. 1 della legge 18 giugno 1931, n. 773, sancisce che \"l\'autorit di pubblica sicurezza  veglia al mantenimento dell\'ordine pubblico, alla sicurezza dei cittadini, alla loro incolumit e alla tutela della propriet; cura l\'osservanza delle leggi e dei regolamenti generali e speciali dello stato, delle province e dei comuni, nonch delle ordinanze delle autorit; presta soccorso nel caso di pubblici e privati infortuni\"; \"per mezzo dei suoi ufficiali, e a richiesta delle parti, provvede alla bonaria composizione dei dissidi privati',1),(448,189,'riducendo il tasso di rischiosit i e aumentando e stabilizzando i flussi di reddito.',1),(449,189,'aumentando i flussi di reddito.',0),(450,189,'riducendo il tasso i di rischiosit del settore e dellazienda.',0),(451,189,' abbassando le spese di ripristino dopo un incidente.',0),(452,190,'descrive le strategie e la pianificazione della security.',0),(453,190,'descrive le linee generali degli obiettivi e le responsabilit delle risorse. ',1),(454,190,'indica chi deve fare che cosa su ogni aspetto della security.',0),(455,191,'riservatezza, immediatezza, disponibilit.',0),(456,191,'riservatezza, integrit, disponibilit.',1),(457,191,'identificazione, autorizzazione, autenticazione.',0),(458,191,'correttezza, identificabilit, tracciabilit.',0),(459,192,'tre mesi',1),(460,192,'novanta giorni',0),(461,192,'mai, se previsto dalla legge',0),(462,193,'luso dellarma economica nella competizione tra paesi',0),(463,193,'luso dellenergia per fare pressioni su altri sistemi economici',0),(464,193,'la salvaguardia della competitivit del sistema economico interno ',0),(465,193,'tutte e tre le precedenti',1),(466,194,'le attivit di monitoraggio degli eventi',0),(467,194,'le attivit che riguardano il travel risk management',0),(468,194,'le attivit di pianificazione e monitoraggio ma anche quelle di mitigazione',1),(469,194,'le attivit a supporto della pianificazione strategica',0),(470,195,'una minaccia potenziale',0),(471,195,'un evento incerto calcolato',1),(472,195,'un evento incerto',0),(473,196,' un incendio causato da un cortocircuito in un macchinario industriale',1),(474,196,'il furto di dati aziendali da parte di un hacker',0),(475,196,'lintrusione non autorizzata di una persona nei locali aziendali',0),(476,197,'attivit e competenze.',0),(477,197,'conoscenze, competenze, abilit.',1),(478,197,'conoscenze.',0),(479,197,'nessuna delle risposte precedenti',0),(480,198,' uno strumento di supporto alla gestione dei progetti.',1),(481,198,' uno strumento di data analysis ',0),(482,198,' uno strumento utilizzato per gli audit di security',0),(483,199,'grado di applicazione delle contromisure, grado di efficienza delle tecnologie, grado di adeguatezza delle risorse umane.',0),(484,199,'probabilit e gravit',0),(485,199,'vulnerabilit e frequenza',1),(486,200,'un rischio strategico.',0),(487,200,'un rischio puro',1),(488,200,'un rischio speculativo',0),(489,201,'il principio della prudenza',0),(490,201,'il principio della competenza',1),(491,201,'il principio della inerenza',0),(492,202,'un livello di potenza limitato',0),(493,202,'la capacit di un soggetto (stato o altro) di attrarre e cooptare, senza luso della forza armata',1),(494,202,' luso della forza armata in maniera contenuta',0),(495,202,'la capacit di un soggetto di usare sia forza militare che forza economica',0),(496,203,'Quinto',0),(497,203,'Primo',0),(498,203,'Secondo',1),(499,204,'Importanza, gravit della situazione',0),(500,204,'Bassa prevedibilit e scarsit tempo di reazione',0),(501,204,'Tutte le caratteristiche precedenti',1),(502,204,'Nessuna delle risposte',0),(503,205,'Livello di rischio ottenuto a prescindere dalla mitigazione operata',1),(504,205,'Rischio effettivo, ovvero il livello di rischio ottenutodopo aver applicato delle contromisure',0),(505,205,'L\'impatto potenziale al verificarsi dell\'evento',0),(506,205,'La gravit dell\'evento rispetto ai valori aziendali',0),(507,206,'Livello di rischio ottenuto a prescindere dalla mitigazione operata',0),(508,206,'Rischio Effettivo, ovvero il livello di rischio  ottenuto dopo aver applicato delle contromisure',1),(509,206,'L\'impatto potenziale al verificarsi dell\'evento',0),(510,206,'La gravit dell\'evento rispetto ai valoro aziendali',0),(511,207,'Propriet intrinseca che potenzialmente pu causare danni ',1),(512,207,'La probabilit che un certo evento si verifichi e l\'impatto che ne puo\' derivare ',0),(513,207,'Elemento che possiede il potenziale di originare il rischio',0),(514,208,'Modello di Ris Financing e poi Risk Controlling',0),(515,208,'Modello Castle Based, Business Oriented e infine Total Risk Governance',1),(516,208,'Modello Castle Based, Perimeter, Security e infine Business Oriented',0),(517,209,'Attivit e competenze',0),(518,209,'Conoscenze, competenze, Abilit',1),(519,209,'Conoscenze',0),(520,209,'Nessuna delle risposte precedenti',0),(524,211,'Evento pianificabile che non abbiamo inserito nella mappa dei rischi',0),(525,211,'Evento con grande impatto che abbiamo previsto',0),(526,211,'Evento inaspettato con grande impatto e che solo a posteriori potr essere reso prevedibile',1),(527,212,'Gli azionisti, le istituzioni e le banche/gli istituti finanziari',0),(528,212,'i dipendenti, i manager, CEO  CDA',0),(529,212,'Fornitori e clienti',0),(530,212,'Tutte e tre le categorie precedenti',1),(531,213,'Evento che pu avere effetto negativo sugli obiettivi, misurato in termini di conseguenze e di probabilit',0),(532,213,'Possibile evento che influenzer negativamente il raggiungimento degli obiettivi',0),(533,213,'Effetto dell\'incertezza in relazione agli obiettivi',1),(534,213,'Situazione o circostanza di incertezza sul raggiungimento degli obiettivi e dei programmi',0),(535,214,'La difesa dei confini, la sopravvivenza della comunit e l\'importanza della dimensione militare',1),(536,214,'La potenza nucleare e le capacit economiche di un paese',0),(537,214,'L\'eliminazione di ogni minaccia economica ',0),(538,215,'La capacit di difesa da \"aggressioni\" esterne in ambito militare',0),(539,215,'La capacit di innovazione di un paese e la sua propensione a creare alleanze militari',0),(540,215,'La capacit di un sistema economico di adattarsi a dei cambiamenti e mantenersi competitivo',1),(541,215,'La capacit di sfruttare la rivalit tra USA e Cina',0),(542,216,'L\'uso dell\' \"arma economica\" nella competizione tra paesi',0),(543,216,'L\'uso dell\'energia per fare pressioni su altri sistemi economici',0),(544,216,'La salvaguardia della competivit del \"sistema economico\" interno',0),(545,216,'Tutte e tre le precedenti',1),(546,217,'lo studio dell\'influenza del dato geografico(fisico, umano, economico...)sulle relazioni tra potenze',1),(547,217,'la disciplina che identifica le relazioni politiche, economiche, sociali e culturali tra dueo pi paesi o , in senso piu ampio tra attori internazionali',0),(548,217,'La disciplina che analizza l\'influenza del dato geografico sulle relazioni tra attori economici, studiando le regole che governano la competizione tra sistemi -Paesi e loro sottosistemi territoriali e settoriali',0),(549,217,'Lo studio dei fenomeni internazionali che influenza le dinamiche politiche di un Sistema Paese',0),(550,218,'25%',0),(551,218,'50%',0),(552,218,'85%',1),(553,218,'65%',0),(554,219,'Russia',0),(555,219,'Arabia Saudita',0),(556,219,'Cina',0),(557,219,'USA',1),(558,220,'Un livello di potenza limitato',0),(559,220,'La capacit di un soggetto (Stato o Altro) di attrarre o cooptare, senza l\'uso della forza armata',1),(560,220,'L\'uso della forza armata in maniera contenuta',0),(561,220,'La capacit di un soggetto sia della forza militare che forza economica',0),(562,221,'Le attivit di monitoraggio degli eventi',0),(563,221,'Le attivit che riguardano il Travel Risk management',0),(564,221,'Le attivit di pianificazione e monitoraggio ma anche quelle di mitigazione',1),(565,221,'Le attivit a supporto della pianificazione strategica',0),(566,222,'Un rischio puro',1),(567,222,'Un rischio speculativo',0),(568,222,'Un rischio strategico',0),(569,223,'Norma ISO 22301',0),(570,223,'Norma ISO UNI 31000',1),(571,223,'Norma UNI 10459',0),(572,224,'Una minaccia potenziale',0),(573,224,'Un evento incero calcolato',1),(574,224,'Un evento incerto',0),(575,225,'Una fonte di rischio che pu portare a conseguenze negative ',1),(576,225,'Un rischio',0),(577,225,'Un evento certo o incerto',0),(578,226,'Un incendio causato da un cortocircuito in un macchinario industriale',1),(579,226,'Il furto di dati aziendali da parte di un hacker',0),(580,226,'L\'intrusione non autorizzata di una persona nei locali aziendali',0),(581,227,'Prevenire incendi legati a comportamenti non conformi alle norme di sicurezza sul lavoro',0),(582,227,'Gestire i rischi derivanti da eventi accidentali come incendi o esplosioni',0),(583,227,'Prevenire e fronteggiare eventi causati da azioni da azioni illecite contro persone e beni',1),(584,228,'E\' una disciplina psicologica che si concentra sulla gestione emotiva dei dipendenti durante un\'emergenza',0),(585,228,'E\' l\'insieme delle attivit volte a contenere i danni e a ripristinare velocemente la situazione precedente alla crisi',1),(586,228,'E\' un metodo per aumentare la produttivit aziendale attraverso la gestione d risorse umane',0),(587,229,'Si occupa solo della fase immediata successiva alla crisi',0),(588,229,'E\' un processo improvvisato che coinvolge solo il top management ',0),(589,229,'E\' un approccio sistematico e preordinato che si sviluppa in pi fasi',1),(593,231,'incendio - furto',1),(594,231,'Infortunio - pandemia - alluvione - terremoto ',1),(595,232,'Probabilit * Vulnerabilit',0),(596,232,'Probabilit * Impatto',1),(597,232,'Vulnerabilit *Frequenza',0),(598,233,'Orientamento al risultato, autocontrollo, pensiero concettuale, sensibilit interpersonale',0),(599,233,'Realizzativa- operativa, sociale, influenza, manageriale, cognitiva, efficacia personale.',1),(600,233,'Manageriali, autocontrollo, pensiero concettuale, comunicazione',0),(601,234,'sono certificazioni volontarie che danno prova dei livelli standard di qualit dei servizi e rivolti alla clientela',0),(602,234,'sono certificazioni obbligatorie richieste dal ministero dell\'interno per valutare la conformit dell\'IVP delle orme di riferimento',1),(603,234,'Sono certificazioni volontarie richieste dal ministero dell\'interno per valutare la conformit dell\'IVP delle orme di riferimento',0),(604,234,'Sono certificazioni rilasciate dal ministero dell\'interno agli istituti di vigilanza che richiedono le licenze di polizia',0),(605,235,'A seguito della sottoscrizione del contratto di lavoro con un istituto di vigilanza privata',0),(606,235,'a seguito del conseguimento del decreto prefettizio di nomina e del giuramento, che avviene presso la prefettura di competenza',1),(607,235,'a seguito del conseguimento del decreto prefettizio di nomina che avviene presso la prefettura di competenza',0),(631,302,'Valutazione dei rischi posti da possibili attacchi terroristici.',0),(632,302,'L’obbligo di valutare tutti i rischi, nessuno escluso; redigere il Documento di valutazione dei rischi (DVR); formare e informare i lavoratori riguardo ai rischi cui possono andare incontro.',1),(633,302,'Il dovere che il datore di lavoro ha di fornire una scorta armata a tutti i viaggiatori in trasferta',0),(634,302,'Il dovere del lavoratore di proteggere e custodire i dati dell’azienda.',0),(635,303,'Gli stranieri all’estero non sono oggetto di rapimento.',0),(636,303,'I rapimenti rappresentano una minaccia concreta per il personale aziendale all’estero e ne esistono differenti tipologie.',1),(637,303,'La microcriminalità è un fattore di rischio che interessa esclusivamente gli stranieri.',0),(638,303,'I gruppi criminali organizzati non rappresentano una minaccia per gli operatori dell’informazione.',0),(639,304,'La documentazione vaccinale richiesta all’ingresso di un Paese.',0),(640,304,'I farmaci necessari a compiere la profilassi antimalarica.',0),(641,304,'Un kit di pronto soccorso.',0),(642,304,'L’insieme dai farmaci che utilizzi quotidianamente, che devi avere con te in caso d’emergenza e quelli necessari ad affrontare la trasferta.',1),(643,305,'Prediligi le connessioni Wi-Fi pubbliche.',0),(644,305,'Evita l’utilizzo di dati criptati, questi potrebbero sovraccaricare il tuo dispositivo.',0),(645,305,'Utilizza password diverse per ogni dispositivo e mantieni sempre attivi i Firewall.',1),(646,305,'Non installare un antivirus, potrebbe facilitare l’azione di eventuali hacker.',0),(647,306,'Comporta la temporanea sospensione (2 mesi) del viaggiatore per nuove trasferte all’estero.',0),(648,306,'Deve essere seguito dall’annullamento del Foglio di Viaggio, che deve avvenire dai 10 ai 15 giorni successivi all’annullamento della missione.',0),(649,306,'Comporta la sospensione del Foglio Viaggio.',1),(650,306,'Deve essere seguito dal tempestivo annullamento del Foglio di Viaggio.',0);
/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `answer_seq`
--

DROP TABLE IF EXISTS `answer_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer_seq`
--

LOCK TABLES `answer_seq` WRITE;
/*!40000 ALTER TABLE `answer_seq` DISABLE KEYS */;
INSERT INTO `answer_seq` VALUES (701);
/*!40000 ALTER TABLE `answer_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedbacks`
--

DROP TABLE IF EXISTS `feedbacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedbacks` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `quiz_id` int DEFAULT NULL,
  `rating` int NOT NULL,
  `comment` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `quiz_id` (`quiz_id`),
  CONSTRAINT `feedbacks_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL,
  CONSTRAINT `feedbacks_ibfk_2` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`) ON DELETE SET NULL,
  CONSTRAINT `feedbacks_chk_1` CHECK (((`rating` >= 1) and (`rating` <= 5)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedbacks`
--

LOCK TABLES `feedbacks` WRITE;
/*!40000 ALTER TABLE `feedbacks` DISABLE KEYS */;
/*!40000 ALTER TABLE `feedbacks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flyway_schema_history`
--

DROP TABLE IF EXISTS `flyway_schema_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flyway_schema_history`
--

LOCK TABLES `flyway_schema_history` WRITE;
/*!40000 ALTER TABLE `flyway_schema_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issued_quiz`
--

DROP TABLE IF EXISTS `issued_quiz`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `issued_quiz` (
  `token_id` binary(32) NOT NULL,
  `issuer_id` int NOT NULL,
  `quiz_id` int DEFAULT NULL,
  `number_of_questions` int NOT NULL,
  `issued_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `expires_at` datetime DEFAULT NULL,
  `duration` time DEFAULT NULL,
  PRIMARY KEY (`token_id`),
  UNIQUE KEY `token_id` (`token_id`),
  KEY `issuer_id` (`issuer_id`),
  KEY `quiz_id` (`quiz_id`),
  CONSTRAINT `issued_quiz_ibfk_1` FOREIGN KEY (`issuer_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `issued_quiz_ibfk_2` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issued_quiz`
--

LOCK TABLES `issued_quiz` WRITE;
/*!40000 ALTER TABLE `issued_quiz` DISABLE KEYS */;
INSERT INTO `issued_quiz` VALUES (_binary '-5E3kq_Hv0wM0ppKtBa5jEfsrm38Cw4F',1,1,10,'2025-10-05 15:15:44',NULL,'00:30:00'),(_binary 'ILy4vNbT-4THoFt9a4bULaZUBdAdhXcC',9,4,5,'2025-10-06 15:26:25','2025-10-07 17:32:00','00:30:00'),(_binary 'KvaRlgJPB-yw9T-0WVO26UgLf5RHu_Tb',1,1,10,'2025-10-05 15:54:36',NULL,'00:30:00'),(_binary 'NVo6c_Knoq6R-JZNTAGCZONZz4RHBdB_',1,1,10,'2025-10-05 15:52:11',NULL,'00:30:00'),(_binary 'WAoLP9ERIfNu09SBXX_QtH1G-jv_ZVgV',9,4,5,'2025-10-05 14:53:23','2025-10-05 17:00:00','00:30:00'),(_binary 'Yknu8L9dbvWMdNByBt6ftO5wRNveu9rf',1,1,14,'2025-10-04 12:14:56',NULL,'00:30:00'),(_binary 'lIFo0Gpz1BFX3x4xlV0iKslYLNhvg2Jw',1,1,10,'2025-10-05 15:51:31',NULL,'00:30:00'),(_binary 'pigmJ_3_JqXUbQ0TzzHXFFS13jLj968W',1,1,10,'2025-10-05 15:23:44',NULL,'00:30:00');
/*!40000 ALTER TABLE `issued_quiz` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `question` varchar(255) NOT NULL,
  `is_multiple_choice` tinyint(1) NOT NULL DEFAULT '0',
  `quiz_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_question_user` (`user_id`),
  KEY `fk_question_quiz` (`quiz_id`),
  CONSTRAINT `fk_question_quiz` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_question_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=307 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (3,'Criminologia','Secondo Edwin Sutherland la criminologia si definisce come:',0,1,1),(4,'Criminologia','La  teoria dell\'associazione differenziale sostiene che',0,1,1),(5,'Criminologia','Il modello Craved di Clarke (1999)  identifica le caratteristiche degli \"Hot products\". Cosa significa la lettera R',0,1,1),(6,'Criminologia','Secondo la Crime Patter Teory , quale delle seguente affermazioni  corretta?',0,1,1),(7,'Criminologia','Nella Routine della Routine Activity, quali sono i tre elementi necessari perch si verifichi un\'opportunit criminale?',0,1,1),(9,'D.Lgs. 231/2001','La responsabilit amministrativa degli enti ai sensi del D.lgs 231/2001 :',0,1,1),(10,'D.Lgs. 231/2001','Quali sono le principali conseguenze di una eventuale condanna della societ ai sensi del D.lg. 231/2001',0,1,1),(52,'D.Lgs. 231/2001','Il modello di organizzazione Gestione e Controllo ex D.Lgs 231/2001 :',0,1,1),(53,'D.Lgs. 231/2001','In caso di segnalazione Whistleblowing il segnalante',0,1,1),(54,'Codice Penale','Un reato si considera commesso in Italia',0,1,1),(55,'Economia e Finanza','Nello stato patrimoniale riclassificato, le voci \"attivo\" e \"passivo\" del bilancio civilistico diventano',0,1,1),(56,'Economia e Finanza','Il tasso interno di investimento di un rendimento',0,1,1),(57,'Economia e Finanza','Il VAN di un investimento ',0,1,1),(58,'Sicurezza Pubblica','Le attribuzioni dell\' autorit nazionale di pubblica sicurezza sono esercitate :',0,1,1),(59,'Economia e Finanza','Il trasferimento a Terzi puo essere',0,1,1),(60,'Sicurezza Privata','La Guardia Particolare Giurata :',0,1,1),(61,'Sicurezza Privata','La figura professionale della Guardia particolare giurata trova fondamento normativo',0,1,1),(62,'Risk Management','Quale  l\'obiettivo prioritario del risk Management',0,1,1),(63,'Risk Management','Obiettivo prioritario della prevenzione',0,1,1),(64,'Criminologia','Cosa  la propensione al rischio?',0,1,1),(65,'Criminologia','L\'obbligo della valutazione del rischio, strumento fondamentale del sistema di prevenzione, riguarda',0,1,1),(66,'Criminologia','Secondo Shein  i livelli della Cultura sono',0,1,1),(67,'Etica','L\'etica',0,1,1),(68,'D.Lgs. 231/2001','Quali tra queste rientra tra le responsabilit dell\'azienda?',0,1,1),(69,'Etica','Nella piramide di Carrol che delinea la responsabilit sociale di impresa',0,1,1),(70,'Criminologia','Il codice Etico di un\'organizzazione',0,1,1),(71,'Risk Management','Definizione del concetto di rischio puro',0,1,1),(72,'Risk management','Un sabotaggio industriale ',0,1,1),(73,'Etica','L\'Etica',0,1,1),(74,'Risk Management','Si definisce pericolo',0,1,1),(103,'Codice Penale','Il taccheggio integra il reato di',0,1,1),(104,'Codice penale','Il diritto di querela si puo\' esercitare',0,1,1),(105,'Codice penale','Il Reato di rapina previsto e punito dall\'art. 628 CP si differenzia dal furto',0,1,1),(106,'Costituzione','A norma dell\' art. 13 della costituzione',0,1,1),(107,'Codice Penale','La facolt di arresto',0,1,1),(108,'Sicurezza Urbana','Quali sono i tre settori di intervento fondamentali in un approccio integrato della sicurezza urbana',0,1,1),(109,'Sicurezza Urbana','Forme ed uso dello spazio hanno un ruolo attivo nel determnare, la sicurezza dei luoghi, su',0,1,1),(110,'Sicurezza Urbana','Per valutare la sicurezza di un luogo  sufficiente monitorare l\'andamento dei reati',0,1,1),(111,'Sicurezza Urbana','Quali di questi elementi si possono considerare elementi utili per valutare la propensione alla sicurezza',0,1,1),(112,'Sicurezza Urbana','Quale di queste affermazioni  vera',0,1,1),(113,'Sicurezza Ambientale','Introdurre i criteri di prevenzione ambientale del crimine  possibile',0,1,1),(114,'Sicurezza Urbana','Un cittadino si sente insicuro quando',0,1,1),(115,'Sicurezza ambientale','Il problema della sicurezza  un argomento che richiede l\'intervento di pi attori in maniera coordinata',0,1,1),(116,'Stress','Condizioni di stress eccessivo',0,1,1),(117,'Stress','Lo stress',0,1,1),(118,'Stress','Lo stress ( seleziona risp errata)',0,1,1),(119,'Stress','Dopo un evento particolarmente attivante o dopo uno sforzo prolungato e sostenuto, il recupero',0,1,1),(120,'Stress Management','Tali interventi di stress management mirano a ridurre o evitare l\'insorgenza dello stress-lavoro correlato . Si tratta di interventi di prevenzione',0,1,1),(121,'Sicurezza','A quali soggetti la normativa affida l\'attuazione e la gestione della salute e sicurezza sul lavoro',0,1,1),(122,'Sicurezza','Quale  lo scopo dell\'analisi dei rischi?',0,1,1),(123,'Sicurezza','La valutazione dei rischi deve essere svolta anche',0,1,1),(124,'Sicurezza','Che cosa  il servizio di Prevenzione e Protezione',0,1,1),(125,'Sicurezza','A chi compete l\'obbligo di vigilanza sul corretto adempimento e rispetto delle regole di sicurezza',0,1,1),(126,'Sicurezza','In relazione al Ruolo di preposto',0,1,1),(127,'Sicurezza','Informazione e  formazione dei viaggiatori aziendali sono',0,1,1),(128,'Sicurezza','Quali attivit vengono richieste per prevenire i rischi da interferenza nelle attivit affidate a terzi',0,1,1),(129,'Sicurezza','Al fine della Tutela  e della salute della sicurezza sul lavoro, la manutenzione',0,1,1),(130,'D.lgs. 231/2001','La responsabilit amministrativa degli enti ai sensi del D.lgs. 231/2001 ',0,1,1),(131,'Economia e finanza','Nello stato patrimoniale riclassificati, le voci \" Attivo\" e  \"passivo\" del bilancio civilistico diventano',0,1,1),(132,'Etica','L\'Etica',0,1,1),(133,'Risk Management','I rischi di natura esterna sono',0,1,1),(134,'Risk management','Nell\'analisi del contesto il vicinato ',0,1,1),(135,'Risk management','Quali tra questi NON  un fattore di contesto interno',0,1,1),(136,'Iso 31000','Quali tra questi NON  uno dei principi fondamentali della ISO 31000',0,1,1),(137,'Risk management','Il costo totale del rischio si calcola tenendo conto di',0,1,1),(138,'Risk management','Cosa significa ERM',0,1,1),(139,'Risk management','Quali sono le principali fasi nel processo di Risk management previste dallo standard UNI?',0,1,1),(140,'Risk Management','Cosa si intende per Analisi del Contesto?',0,1,1),(141,'Risk management','Il rischio  uguale a',0,1,1),(142,'Risk management','Le tipologie di rischio si possono classificare con quali tassonomie',0,1,1),(143,'Risk management','La probabilit di accadimento di un rischio  funzione di:',0,1,1),(144,'Risk management','La vulnerabilit  funzione di',0,1,1),(145,'Risk management','Quale  l\'obiettivo prioritario del risk management?',0,1,1),(146,'Risk management','Obiettivo prioritario della protezione',0,1,1),(147,'Risk management','Obiettivo prioritario della prevenzione',0,1,1),(148,'Risk management','Il trasferimento a terzi pu essere',0,1,1),(149,'Risk management','Un sistema ERM (Enterprise risk management)',0,1,1),(150,'Sicurezza Organizzativa','Gli strumenti di sicurezza organizzativa sono:',0,1,1),(151,'Risk management','Con l\'implementazione razionale di un sistema di ERM in aziende si pu:',0,1,1),(152,'Risk management','Cosa  il diagramma di Gantt',0,1,1),(153,'Risk management','La sicurezza economica prevede anche',0,1,1),(154,'Security manager','Considerando l\'attivit di Security manager, l\'analisi sul rischio geopolitico riguarda: che il security manager deve considerare:',0,1,1),(155,'Risk management','In che modo le attivit di sicurezza incidono sul valore economico di un\'impresa',0,1,1),(156,'Security','Come si articola di solito la security aziendale?',0,1,1),(157,'Security','La policy nella funzione della security  un documento che',0,1,1),(158,'Security','Sicurezza delle informazioni - Con il termine \"parametri di sicurezza\" si fa riferimento ai seguenti attributi dell\'informazione',0,1,1),(159,'Security','Quali sono le principali categorie di strumenti per la sicurezza delle informazioni:',0,1,1),(160,'Security','Un sabotaggio industriale :',0,1,1),(161,'Risk management','Quali sono le (4+1) principali politiche della gestione del rischio?',0,1,1),(162,'Contabilit','Quale tra i seguenti principi contabili definisce la differenza tra una OPEX e una CAPEX',0,1,1),(163,'Security','I servizi ausiliari alla sicurezza sono',0,1,1),(165,'IVP','la guardia particolare giurata :*',0,1,1),(166,'IVP','La licenza per gestire un istituto di vigilanza privata  rilasciata*',0,1,1),(168,'Economia e finanza','I tipi di conti economici riclassificati sono',0,1,1),(169,'Economia e Finanza','La leva operativa consente di',0,1,1),(170,'Economia e Finanza','Nello stato patrimoniale riclassificato, le voci attivo e passivo del bilancio civilistico diventano',0,1,1),(171,'Economia e finanza','Le capex hanno queste caratteristiche (solo una  corretta):*',0,1,1),(173,'Economia e Finanza','il tasso interno di rendimento di un investimento :*',0,1,1),(174,'Economia e Finanza','il van di un investimento :*',0,1,1),(179,'TULPS','perch lordine e la sicurezza pubblica sono prerogative dei pubblici poteri?*',0,1,1),(182,'pubblica sicurezza','La norma dellart 13 della costituzione, le attivit di detenzione di ispezione o perquisizione personale:',0,1,1),(183,'TULPS','Le attribuzioni dell\'autorit nazionale di pubblica sicurezza sono esercitate:',0,1,1),(186,'Security','La figura professionale della guardia particolare giurata trova fondamento normativo',0,1,1),(187,'TULPS','la qualifica di pubblico ufficiale, ai sensi dell\'art. 357 c.p., deve esser riconosciuta:*',0,1,1),(188,'TULPS','Perch lordine e la sicurezza pubblica sono prerogative dei pubblici poteri?',0,1,1),(189,'Economia e Finanza','in che modo le attivit di sicurezza incidono sul valore economico di unimpresa',0,1,1),(190,'Security','La policy nella funzione di security  un documento che',0,1,1),(191,'Information Security','Sicurezza delle informazioni - con il termine \"parametri di sicurezza\" si fa riferimento ai seguenti attributi dell\'informazione',0,1,1),(192,'Codice Penale','Il diritto di querela si esaurisce in',0,1,1),(193,'Security','La sicurezza economica prevede anche',0,1,1),(194,'Security','Considerando lattivit di un security manager, lanalisi sul rischio geopolitico riguarda: il security manager deve considerare',0,1,1),(195,'Risk Management','Il rischio ',0,1,1),(196,'Safety','Quale tra le seguenti situazioni rientra nel concetto di safety',0,1,1),(197,'Security manager','La norma uni10459: 2017 descrive del professionista della security',0,1,1),(198,'Security Manager','Cos il diagramma di gantt',0,1,1),(199,'Risk Management','La probabilit di accadimento di un rischio  funzione di',0,1,1),(200,'Risk management','Un sabotaggio industriale ',0,1,1),(201,'Economia e Finanza','Quale tra i seguenti principi contabili definisce la differenza tra una opex e una capex',0,1,1),(202,'Risck management','per soft power si intende',0,1,1),(203,'Security','Nella piramide di Maslow quale livello occupa il bisogno di sicurezza?',0,1,1),(204,'Security','Quali sono le caratteristiche tipiche di un evento per essere considerato crisi in un\'azienda secondo le teorie esposte:',0,1,1),(205,'Risk management','Cosa si intende per rischio intrinseco',0,1,1),(206,'Risk management','Cosa si intende per rischio residuo',0,1,1),(207,'Risk management','Si definisce Pericolo',0,1,1),(208,'Security manager','Come si  evoluta negli anni la funzione di security',0,1,1),(209,'UNI 10459:2017','La norma UNI 10459:2017 descrive del Professionista della Security',0,1,1),(211,'Risk management','Cosa si intende per cigno nero',0,1,1),(212,'Risk management','Chi sono gli stakeholder in un\'azienda?',0,1,1),(213,'UNI ISO 31000','La UNI ISO 31000 come definisce il rischio?',0,1,1),(214,'Geopolitica','Il concetto di \"tradizionale\" di sicurezza nazionale si basa su',0,1,1),(215,'Geopolitica','La sicurezza economica ',0,1,1),(216,'Geopolitica','La sicurezza economica prevede anche',0,1,1),(217,'Geopolitica','Quale delle seguenti definizioni corrisponde al termine \"geopolitica\"',0,1,1),(218,'Geopolitica','Stando alle proiezioni elaborate dalle Nazioni Unite, entro il 2100 quanta parte della popolazione mondiale vivr in stati che non rientrano nella categoria \" Occidente\"',0,1,1),(219,'Geopolitica','Quale tra questi Paesi  il principale produttore mondiale di petrolio e gas naturale:',0,1,1),(220,'Geopolitica','Per \" soft power\" si intende:',0,1,1),(221,'Security Manager','Considerando l\'attivit di un security manager, l\'analisi sul rischio geopolitico riguarda: che il secuirity manager deve considerare',0,1,1),(222,'Risk management','Un disastro naturale ',0,1,1),(223,'UNI ISO 31000','Lo standard sulla gestione dei rischi',0,1,1),(224,'UNI ISO 31000','Il rischio ',0,1,1),(225,'UNI ISO 31000','Una minaccia ',0,1,1),(226,'Safety','Quale tra le seguenti situazioni rientra nel concetto di safety',0,1,1),(227,'Security','La security si occupa principalmente di',0,1,1),(228,'Crisis Management','Quale tra le seguenti affermazioni definisce correttamente il Crisis Management nel contesto dei management',0,1,1),(229,'Crisis management','Quale tra le seguenti caratteristiche  propria del Crisis Management',0,1,1),(231,'Rischio Puro esempio','Fare un esempio di rischio puro',1,1,1),(232,'Risk management','Il rischio  uguale a:',0,1,1),(233,'Security Manager','Le abilit o people skills di un professionista della security si possono distinguere in quali categorie?',0,1,1),(234,'IVP','Le certificazioni di cui allart. 6 dm 115 4 giugno 2014',0,1,1),(235,'IVP','La guardia particolare giurata diviene tale',0,1,1),(302,'Quadro normativo','1) Il quadro normativo italiano definisce il dovere di protezione come:',0,4,9),(303,'Rischi di natura criminosa','Solo una delle seguenti affermazioni circa il rischio posto dai fenomeni di natura criminosa è corretta',0,4,9),(304,'Nozioni di Primo Soccorso','Cosa s’intende per farmacia da viaggio?',0,4,9),(305,'Rischi digitali','I rischi digitali rappresentano una minaccia concreta per una trasferta. Quali sono le misure di mitigazione che devono essere attuate?',0,4,9),(306,'Norme aziendali','Secondo quanto previsto dalle norme aziendali, l’annullamento di una trasferta:',0,4,9);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question_seq`
--

DROP TABLE IF EXISTS `question_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question_seq`
--

LOCK TABLES `question_seq` WRITE;
/*!40000 ALTER TABLE `question_seq` DISABLE KEYS */;
INSERT INTO `question_seq` VALUES (401);
/*!40000 ALTER TABLE `question_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quiz`
--

DROP TABLE IF EXISTS `quiz`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quiz` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` text,
  `user_id` int NOT NULL,
  `questions_count` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `quiz_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quiz`
--

LOCK TABLES `quiz` WRITE;
/*!40000 ALTER TABLE `quiz` DISABLE KEYS */;
INSERT INTO `quiz` VALUES (1,'Sample Quiz','This is a sample quiz description.',1,150),(4,'Corso Travel Security - Rai Com S.p.A',NULL,9,5);
/*!40000 ALTER TABLE `quiz` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_quiz_attempt`
--

DROP TABLE IF EXISTS `user_quiz_attempt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_quiz_attempt` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `token` binary(32) NOT NULL,
  `score` int DEFAULT NULL,
  `attempted_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `finished_at` timestamp NULL DEFAULT NULL,
  `status` enum('IN_PROGRESS','COMPLETED') NOT NULL DEFAULT 'IN_PROGRESS',
  `questions` json DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `token` (`token`),
  CONSTRAINT `user_quiz_attempt_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_quiz_attempt_ibfk_2` FOREIGN KEY (`token`) REFERENCES `issued_quiz` (`token_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_quiz_attempt`
--

LOCK TABLES `user_quiz_attempt` WRITE;
/*!40000 ALTER TABLE `user_quiz_attempt` DISABLE KEYS */;
INSERT INTO `user_quiz_attempt` VALUES (2,1,_binary 'Yknu8L9dbvWMdNByBt6ftO5wRNveu9rf',NULL,'2025-10-04 12:16:18',NULL,'IN_PROGRESS',NULL),(5,6,_binary 'WAoLP9ERIfNu09SBXX_QtH1G-jv_ZVgV',NULL,'2025-10-05 14:53:44',NULL,'IN_PROGRESS',NULL),(6,1,_binary 'WAoLP9ERIfNu09SBXX_QtH1G-jv_ZVgV',NULL,'2025-10-05 14:56:10',NULL,'IN_PROGRESS',NULL),(9,1,_binary '-5E3kq_Hv0wM0ppKtBa5jEfsrm38Cw4F',NULL,'2025-10-05 15:15:45',NULL,'IN_PROGRESS',NULL),(10,1,_binary 'pigmJ_3_JqXUbQ0TzzHXFFS13jLj968W',NULL,'2025-10-05 15:23:45',NULL,'IN_PROGRESS',NULL),(12,1,_binary 'lIFo0Gpz1BFX3x4xlV0iKslYLNhvg2Jw',NULL,'2025-10-05 15:51:33',NULL,'IN_PROGRESS',NULL),(13,1,_binary 'NVo6c_Knoq6R-JZNTAGCZONZz4RHBdB_',NULL,'2025-10-05 15:52:13',NULL,'IN_PROGRESS',NULL),(14,1,_binary 'KvaRlgJPB-yw9T-0WVO26UgLf5RHu_Tb',NULL,'2025-10-05 15:54:42',NULL,'IN_PROGRESS',NULL),(16,11,_binary 'WAoLP9ERIfNu09SBXX_QtH1G-jv_ZVgV',NULL,'2025-10-05 15:59:21',NULL,'IN_PROGRESS',NULL);
/*!40000 ALTER TABLE `user_quiz_attempt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `profile_picture_url` varchar(2048) DEFAULT NULL,
  `role` enum('USER','ADMIN') NOT NULL DEFAULT 'USER',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Claudia','{bcrypt}$2a$12$h3J.W9HPGt.MCdhgnYNDiuFuqvn1yfDmZIJFOJTD5VLCmXPzxNP4C','david.cascella.5@gmail.com',1,NULL,'USER'),(2,'Admin','{bcrypt}$2a$12$h3J.W9HPGt.MCdhgnYNDiuFuqvn1yfDmZIJFOJTD5VLCmXPzxNP4C','david.cascellaa.5@gmail.com',1,NULL,'ADMIN'),(6,'gino','{bcrypt}$2a$12$aT0zx/i2V0SLhqtHvKA5BO6LUnfFWzR31EbNfUDFPrkHXksvaV2Kq','twitch.david.cascella.5@gmail.com',1,NULL,'USER'),(7,'Sarahaaa','{bcrypt}$2a$12$LepXyK36ivy7v8ck5bsYae1owAMyS7vDmslI.2LRb7uMzFS7QzhSO','shnettis@gmail.com',1,NULL,'USER'),(8,'Claudia Pisa',NULL,'ing.pisaclaudia@gmail.com',1,'https://lh3.googleusercontent.com/a/ACg8ocIHusomYOilby_4mIYeHmmCym5eTHhTSQ_f9LuZ_HWD2y95Qw=s96-c','USER'),(9,'ClaudiaP','{bcrypt}$2a$12$3xrGHkDEE2qG/Juv3Td0MuqWr6Wx99Td0DK366FfKReUmqtK7TovS','claudia.pisa.collaboratore@rai.it',1,NULL,'USER'),(10,'Luigi','{bcrypt}$2a$12$wPkL2K46TGrnTP76ymt.E.kEz4qb2mdWWU6cilkBxAhFixWLJGJtq','luigi.cascella@rai.it',1,NULL,'USER'),(11,'Davide Cascella',NULL,'dev.davide.cascella.5@gmail.com',1,'https://lh3.googleusercontent.com/a/ACg8ocJ1a59_WL2fDuWIlxwvfrCwHSHbHrhoyxQdXuYHbdM_YIEbHA=s96-c','USER');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-06 17:33:39
