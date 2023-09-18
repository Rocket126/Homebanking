package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardControllers {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;


    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> register(Authentication authentication,
     @RequestParam CardType cardType, @RequestParam CardColor cardColor) {

        Client client = clientRepository.findByEmail(authentication.getName());

        if(client.getCards().stream().filter(card -> card.getType() == cardType).count() >= 3 )
        {
            return new ResponseEntity<>("no debe superar mas de 3 tarjetas por tipo", HttpStatus.FORBIDDEN);
        }

        int cantidadTarjetas = client.getCards().size();//cantidad de tarjetas del usuario actual

        if (cantidadTarjetas > 6) {
            return new ResponseEntity<>("El cliente no puede poseer mas de 6 tarjetas", HttpStatus.FORBIDDEN);
        }


        int ccv = (int) ((Math.random() * 900+100));//le otorgo un numero aleatorio al CCV de 3 cifras

        int[] v = new int[4];

        for(int i=0; i<v.length;i++)
        {
            v[i]= (int) (Math.random() * 9000+1000);//generando numeros de de 4 cifras
        }
        String numero=""+v[0]+"-"+v[1]+"-"+v[2]+"-"+v[3];

       cardRepository.save(new Card(client.getFirstName()+" "+client.getLastName(), cardType,
               cardColor,numero,ccv, LocalDateTime.now().plusYears(5),LocalDateTime.now(), client));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    //OBTENGO TODAS LAS TARJETAS POR EL USUARIO QUE SE ENCUENTRA EN SESSION
    @GetMapping("/clients/current/cards")
     public List<CardDTO> getCards(Authentication authentication) {
            Client client = this.clientRepository.findByEmail(authentication.getName());
            return client.getCards().stream().map(CardDTO::new).collect(Collectors.toList());
     }




}
