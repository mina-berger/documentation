package com.mina.documentation;

import com.mina.documentation.linguistique.Lang;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author minaberger
 */
public class DocUtil {
  public static HashMap<Character, String> BETACODE = new HashMap<>();
  static{
    BETACODE.put('α', "a");    BETACODE.put('ά', "a/");   BETACODE.put('ὰ', "a\\");  BETACODE.put('ᾶ', "a=");
    BETACODE.put('ἀ', "a)");   BETACODE.put('ἁ', "a(");   BETACODE.put('ἄ', "a)/");  BETACODE.put('ἅ', "a(/");  
    BETACODE.put('ἂ', "a)\\"); BETACODE.put('ἃ', "a(\\"); BETACODE.put('ἆ', "a)=");  BETACODE.put('ἇ', "a(=");
    BETACODE.put('ᾳ', "a|");   BETACODE.put('ᾴ', "a/|");  BETACODE.put('ᾲ', "a\\|"); BETACODE.put('ᾷ', "a=|"); 
    BETACODE.put('ᾀ', "a)|");  BETACODE.put('ᾁ', "a(|");  
    BETACODE.put('ᾄ', "a)/|"); BETACODE.put('ᾅ', "a(/|"); BETACODE.put('ᾂ', "a)\\|"); BETACODE.put('ᾃ', "a(\\|");
    BETACODE.put('ᾆ', "a)=|"); BETACODE.put('ᾇ', "a(=|");
    
    BETACODE.put('β', "b");
    BETACODE.put('γ', "g");
    BETACODE.put('δ', "d");
    
    BETACODE.put('ε', "e");   BETACODE.put('έ', "e/");  BETACODE.put('ὲ', "e\\");  BETACODE.put('ἐ', "e)"); 
    BETACODE.put('ἑ', "e(");   
    BETACODE.put('ἔ', "e)/"); BETACODE.put('ἕ', "e(/"); BETACODE.put('ἒ', "e)\\"); BETACODE.put('ἓ', "e(\\");

    BETACODE.put('ζ', "z");
    
    BETACODE.put('η', "h");    BETACODE.put('ή', "h/");    BETACODE.put('ὴ', "h\\");   BETACODE.put('ῆ', "h=");
    BETACODE.put('ἠ', "h)");   BETACODE.put('ἡ', "h(");    BETACODE.put('ἤ', "h)/");   BETACODE.put('ἥ', "h(/");
    BETACODE.put('ἢ', "h)\\"); BETACODE.put('ἣ', "h(\\");  BETACODE.put('ἦ', "h)=");   BETACODE.put('ἧ', "h(="); 
    BETACODE.put('ῃ', "h|");   BETACODE.put('ῄ', "h/|");   BETACODE.put('ῂ', "h\\|");  BETACODE.put('ῇ', "h=|");
    BETACODE.put('ᾐ', "h)|");  BETACODE.put('ᾑ', "h(|");  
    BETACODE.put('ᾔ', "h)/|"); BETACODE.put('ᾒ', "h)\\|"); BETACODE.put('ᾒ', "h)\\|"); BETACODE.put('ᾓ', "h(\\|"); 
    BETACODE.put('ᾖ', "h)=|"); BETACODE.put('ᾗ', "h(=|");

    BETACODE.put('θ', "q");
    
    BETACODE.put('ι', "i");    BETACODE.put('ί', "i/");  BETACODE.put('ὶ', "i\\"); BETACODE.put('ἰ', "i)"); 
    BETACODE.put('ἱ', "i(");   BETACODE.put('ῖ', "i="); 
    BETACODE.put('ἴ', "i)/");  BETACODE.put('ἵ', "i(/"); BETACODE.put('ἲ', "i)\\"); 
    BETACODE.put('ἳ', "i(\\"); BETACODE.put('ἶ', "i)="); 
    BETACODE.put('ἷ', "i(=");
    
    BETACODE.put('κ', "k");
    BETACODE.put('λ', "l");
    BETACODE.put('μ', "m");
    BETACODE.put('ν', "n");
    BETACODE.put('ξ', "c");

    BETACODE.put('ο', "o");   BETACODE.put('ό', "o/");  BETACODE.put('ὸ', "o\\"); BETACODE.put('ὀ', "o)"); 
    BETACODE.put('ὁ', "o(");   
    BETACODE.put('ὄ', "o)/"); BETACODE.put('ὅ', "o(/"); BETACODE.put('ὂ', "o)\\"); BETACODE.put('ὃ', "o(\\");

    BETACODE.put('π', "p");
    BETACODE.put('ρ', "r"); BETACODE.put('ῤ', "r("); BETACODE.put('ῥ', "r("); 
    BETACODE.put('σ', "s");
    BETACODE.put('ς', "s");
    BETACODE.put('τ', "t");
    
    BETACODE.put('υ', "u");   BETACODE.put('ύ', "u/"); BETACODE.put('ὺ', "u\\"); BETACODE.put('ὐ', "u)"); 
    BETACODE.put('ὑ', "u(");  BETACODE.put('ῦ', "u="); 
    BETACODE.put('ὔ', "u)/"); BETACODE.put('ὕ', "u(/"); BETACODE.put('ὒ', "u)\\"); 
    BETACODE.put('ὓ', "u(\\");BETACODE.put('ὖ', "u)="); 
    BETACODE.put('ὗ', "u(=");

    BETACODE.put('φ', "f");
    BETACODE.put('χ', "x");
    BETACODE.put('ψ', "y");
    
    BETACODE.put('ω', "w");    BETACODE.put('ώ', "w/");   BETACODE.put('ὼ', "w\\");   BETACODE.put('ῶ', "w=");
    BETACODE.put('ὠ', "w)");   BETACODE.put('ὡ', "w(");   BETACODE.put('ὤ', "w)/");   BETACODE.put('ὥ', "w(/");
    BETACODE.put('ὢ', "w)\\"); BETACODE.put('ὣ', "w(\\"); BETACODE.put('ὦ', "w)=");  BETACODE.put('ὧ', "w(=");
    BETACODE.put('ῳ', "w|");   BETACODE.put('ῴ', "w/|");  BETACODE.put('ῲ', "w\\|");  BETACODE.put('ῷ', "w=|");
    BETACODE.put('ᾠ', "w)|");  BETACODE.put('ᾡ', "w(|"); 
    BETACODE.put('ᾤ', "w)/|"); BETACODE.put('ᾥ', "w(/|"); BETACODE.put('ᾢ', "w)\\|"); BETACODE.put('ᾣ', "w(\\|");
    BETACODE.put('ᾦ', "w)=|"); BETACODE.put('ᾧ', "w(=|");
    
    BETACODE.put('Α', "*a");     BETACODE.put('Ά', "*/a");   BETACODE.put('Ὰ', "*\\a");  BETACODE.put('Ἀ', "*)a"); 
    BETACODE.put('Ἁ', "*(a");    BETACODE.put('Ἄ', "*)/a");  BETACODE.put('Ἅ', "*(/a");  BETACODE.put('Ἂ', "*)\\a"); 
    BETACODE.put('Ἃ', "*(\\a");  BETACODE.put('Ἆ', "*)=a");  BETACODE.put('Ἇ', "*(=a");
    BETACODE.put('ᾼ', "*a|");    BETACODE.put('ᾈ', "*)a|"); 
    BETACODE.put('ᾉ', "*(a|");   BETACODE.put('ᾌ', "*)/a|"); BETACODE.put('ᾍ', "*(/a|"); BETACODE.put('ᾊ', "*)\\a|");
    BETACODE.put('ᾋ', "*(\\a|"); BETACODE.put('ᾎ', "*)=a|"); BETACODE.put('ᾏ', "*(=a|");
    
    BETACODE.put('Β', "*b");
    BETACODE.put('Γ', "*g");
    BETACODE.put('Δ', "*d");

    BETACODE.put('Ε', "*e");   BETACODE.put('Έ', "*/e");  BETACODE.put('Ὲ', "*\\e");  BETACODE.put('Ἐ', "*)e"); 
    BETACODE.put('Ἑ', "*(e");   
    BETACODE.put('Ἔ', "*)/e"); BETACODE.put('Ἕ', "*(/e"); BETACODE.put('Ἒ', "*)\\e"); BETACODE.put('Ἓ', "*(\\e");

    BETACODE.put('Ζ', "*z");

    BETACODE.put('Η', "*h");     BETACODE.put('Ή', "*/h");   BETACODE.put('Ὴ', "*\\h");   BETACODE.put('Ἠ', "*)h");
    BETACODE.put('Ἡ', "*(h");    BETACODE.put('Ἤ', "*)/h");  BETACODE.put('Ἥ', "*(/h");   BETACODE.put('Ἢ', "*)\\h");
    BETACODE.put('Ἣ', "*(\\h");  BETACODE.put('Ἦ', "*)=h");  BETACODE.put('Ἧ', "*(=h"); 
    BETACODE.put('ῌ', "*h|");    BETACODE.put('ᾘ', "*)h|"); 
    BETACODE.put('ᾙ', "*(h|");   BETACODE.put('ᾜ', "*)/h|"); BETACODE.put('ᾚ', "*)\\h|"); BETACODE.put('ᾚ', "*)\\h|");
    BETACODE.put('ᾛ', "*(\\h|"); BETACODE.put('ᾞ', "*)=h|"); BETACODE.put('ᾟ', "*(=h|");
 
    BETACODE.put('Θ', "*q");
    
    BETACODE.put('Ι', "*i");    BETACODE.put('Ί', "*/i");  BETACODE.put('Ὶ', "*\\i"); BETACODE.put('Ἰ', "*)i"); 
    BETACODE.put('Ἱ', "*(i");   
    BETACODE.put('Ἴ', "*)/i");  BETACODE.put('Ἵ', "*(/i"); BETACODE.put('Ἲ', "*)\\i"); 
    BETACODE.put('Ἳ', "*(\\i"); BETACODE.put('Ἶ', "*)=i"); 
    BETACODE.put('Ἷ', "*(=i");
    
    BETACODE.put('Κ', "*k");
    BETACODE.put('Λ', "*l");
    BETACODE.put('Μ', "*m");
    BETACODE.put('Ν', "*n");
    BETACODE.put('Ξ', "*c");
    
    BETACODE.put('Ο', "*o");   BETACODE.put('Ό', "*/o");  BETACODE.put('Ὸ', "*\\o"); BETACODE.put('Ὀ', "*)o"); 
    BETACODE.put('Ὁ', "*(o");   
    BETACODE.put('Ὄ', "*)/o"); BETACODE.put('Ὅ', "*(/o"); BETACODE.put('Ὂ', "*)\\o"); BETACODE.put('Ὃ', "*(\\o");

    BETACODE.put('Π', "*p");
    BETACODE.put('Ρ', "*r"); BETACODE.put('Ῥ', "*)r");
    BETACODE.put('Σ', "*s");
    BETACODE.put('Τ', "*t");


    BETACODE.put('Υ', "*u");  BETACODE.put('Ύ', "u/");   BETACODE.put('Ύ', "*\\u"); 
    BETACODE.put('Ὑ', "*(u"); BETACODE.put('Ὕ', "*(/u"); BETACODE.put('Ὓ', "*(\\u"); BETACODE.put('Ὗ', "*(=u");

    BETACODE.put('Φ', "*f");
    BETACODE.put('Χ', "*x");
    BETACODE.put('Ψ', "*y");
    
    BETACODE.put('Ω', "*w");     BETACODE.put('Ώ', "*/w");   BETACODE.put('Ὼ', "*\\w");  BETACODE.put('Ὠ', "*)w");
    BETACODE.put('Ὡ', "*(w");    BETACODE.put('Ὤ', "*)/w");  BETACODE.put('Ὥ', "*(/w");  BETACODE.put('Ὢ', "*)\\w");
    BETACODE.put('Ὣ', "*(\\w");  BETACODE.put('Ὦ', "*)=w");  BETACODE.put('Ὧ', "*(=w");
    BETACODE.put('ῼ', "*w|");    BETACODE.put('ᾨ', "*)w|");
    BETACODE.put('ᾩ', "*(w|");   BETACODE.put('ᾬ', "*)/w|"); BETACODE.put('ᾭ', "*(/w|"); BETACODE.put('ᾪ', "*)\\w|");
    BETACODE.put('ᾫ', "*(\\w|"); BETACODE.put('ᾮ', "*)=w|"); BETACODE.put('ᾯ', "*(=w|");

  }
  static List<Character> SKIPBETACODE = Arrays.asList(' ');
  public static String toBetacode(String str){
    StringBuffer sb = new StringBuffer();
    for(char c:str.toCharArray()){
      if(BETACODE.containsKey(c)){
        sb.append(BETACODE.get(c));
      }else if(SKIPBETACODE.contains(c)){
        sb.append(c);
      }else if(Character.toString(c).matches("[0-9a-zA-Z:'᾽,.\n]")){
        sb.append(c);
      }else{
        System.out.println("BETACODE:unknown char:" + c);
        sb.append(c);
      }
      
    }
    return sb.toString();
  }
  public static String washBy(Lang lang, String str){
    if(lang.equals(Lang.GR)){
      if(str.startsWith("δ’")){
        str = str.substring(2).trim();
      }
      str = str.replaceAll("ὰ", "ά").replaceAll("ἂ", "ἄ").replaceAll("ἃ", "ἄ")
            .replaceAll("ὲ", "έ").replaceAll("ἒ", "ἔ").replaceAll("ἓ", "ἕ")
            .replaceAll("ὴ", "ή").replaceAll("ἢ", "ἤ").replaceAll("ἣ", "ἥ")
            .replaceAll("ὶ", "ί").replaceAll("ἲ", "ἴ").replaceAll("ἳ", "ἵ")
            .replaceAll("ὼ", "ώ").replaceAll("ὢ", "ὤ").replaceAll("ὣ", "ὥ")
            .replaceAll("ὺ", "ύ").replaceAll("ὒ", "ὔ").replaceAll("ὓ", "ὕ");

    }
    return str;
    
  }

  public static String simple(String str) {
    str = str.toLowerCase()
      .replaceAll("ā", "a").replaceAll("ē", "e").replaceAll("ī", "i").replaceAll("ō", "o").replaceAll("ū", "u")
      .replaceAll("ă", "a").replaceAll("ĕ", "e").replaceAll("ĭ", "i").replaceAll("ŏ", "o").replaceAll("ŭ", "u")
      .replaceAll("ǎ", "a").replaceAll("ě", "e").replaceAll("ǐ", "i").replaceAll("ǒ", "o").replaceAll("ǔ", "u").trim();
    if (str.endsWith("!")) {
      str = str.substring(0, str.length() - 1).trim();
    }
    return str;
  }
  public static String wash(String str){
    str = str.trim();
    while(
      str.startsWith("\"") || str.startsWith("/") || str.startsWith("(") || str.startsWith("[") || 
      str.startsWith("'") || str.startsWith("¿") || str.startsWith("¡")){
      str = str.substring(1).trim();
    }
    while(str.endsWith("\"") || str.endsWith(".") || str.endsWith(",") || str.endsWith("?") || 
       str.endsWith("!") || str.endsWith(";") || str.endsWith(":") || str.endsWith("-") ||
       str.endsWith(")") || str.endsWith("]") || str.endsWith("'") || str.endsWith("…") ||
       str.endsWith("᾽") || str.endsWith("·")){
      str = str.substring(0, str.length() - 1).trim();
    }
    return str;
  }
  public static void main(String[] args){
    System.out.println(toBetacode(
      "οὐκ ἄρα μοῦνον ἔην Ἐρίδων γένος, ἀλλ᾽ ἐπὶ γαῖαν \n" +
"εἰσὶ δύω: τὴν μέν κεν ἐπαινέσσειε νοήσας, \n" +
"ἣ δ᾽ ἐπιμωμητή: διὰ δ᾽ ἄνδιχα θυμὸν ἔχουσιν. \n" +
"ἣ μὲν γὰρ πόλεμόν τε κακὸν καὶ δῆριν ὀφέλλει, \n" +
"15σχετλίη: οὔτις τήν γε φιλεῖ βροτός, ἀλλ᾽ ὑπ᾽ ἀνάγκης \n" +
"ἀθανάτων βουλῇσιν Ἔριν τιμῶσι βαρεῖαν. \n" +
"τὴν δ᾽ ἑτέρην προτέρην μὲν ἐγείνατο Νὺξ ἐρεβεννή, \n" +
"θῆκε δέ μιν Κρονίδης ὑψίζυγος, αἰθέρι ναίων, \n" +
"γαίης ἐν ῥίζῃσι, καὶ ἀνδράσι πολλὸν ἀμείνω: \n" +
"20ἥτε καὶ ἀπάλαμόν περ ὁμῶς ἐπὶ ἔργον ἔγειρεν. \n" +
"εἰς ἕτερον γάρ τίς τε ἰδὼν ἔργοιο χατίζει \n" +
"πλούσιον, ὃς σπεύδει μὲν ἀρώμεναι ἠδὲ φυτεύειν \n" +
"οἶκόν τ᾽ εὖ θέσθαι: ζηλοῖ δέ τε γείτονα γείτων \n" +
"εἰς ἄφενος σπεύδοντ᾽: ἀγαθὴ δ᾽ Ἔρις ἥδε βροτοῖσιν. \n" +
"25καὶ κεραμεὺς κεραμεῖ κοτέει καὶ τέκτονι τέκτων, \n" +
"καὶ πτωχὸς πτωχῷ φθονέει καὶ ἀοιδὸς ἀοιδῷ. \n" +
"\n" +
"ὦ Πέρση, σὺ δὲ ταῦτα τεῷ ἐνικάτθεο θυμῷ, \n" +
"μηδέ σ᾽ Ἔρις κακόχαρτος ἀπ᾽ ἔργου θυμὸν ἐρύκοι \n" +
"νείκε᾽ ὀπιπεύοντ᾽ ἀγορῆς ἐπακουὸν ἐόντα. \n" +
"30ὤρη γάρ τ᾽ ὀλίγη πέλεται νεικέων τ᾽ ἀγορέων τε, \n" +
"ᾧτινι μὴ βίος ἔνδον ἐπηετανὸς κατάκειται \n" +
"ὡραῖος, τὸν γαῖα φέρει, Δημήτερος ἀκτήν. \n" +
"τοῦ κε κορεσσάμενος νείκεα καὶ δῆριν ὀφέλλοις \n" +
"κτήμασ᾽ ἐπ᾽ ἀλλοτρίοις: σοὶ δ᾽ οὐκέτι δεύτερον ἔσται \n" +
"35ὧδ᾽ ἔρδειν: ἀλλ᾽ αὖθι διακρινώμεθα νεῖκος \n" +
"ἰθείῃσι δίκῃς, αἵ τ᾽ ἐκ Διός εἰσιν ἄρισται. \n" +
"ἤδη μὲν γὰρ κλῆρον ἐδασσάμεθ᾽, ἀλλὰ τὰ πολλὰ \n" +
"ἁρπάζων ἐφόρεις μέγα κυδαίνων βασιλῆας \n" +
"δωροφάγους, οἳ τήνδε δίκην ἐθέλουσι δίκασσαι. \n" +
"40νήπιοι, οὐδὲ ἴσασιν ὅσῳ πλέον ἥμισυ παντὸς \n" +
"οὐδ᾽ ὅσον ἐν μαλάχῃ τε καὶ ἀσφοδέλῳ μέγ᾽ ὄνειαρ."));
  }

}
