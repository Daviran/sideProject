package edi.editor.ediApi.controller;

import edi.editor.ediApi.utils.BaplieParser;
import edi.editor.ediApi.utils.CodecoParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api/parse")
public class EdiController {

    @PostMapping("/baplie")
    public ResponseEntity<String> parseBaplie(@RequestParam("file") MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            BaplieParser.BaplieData baplieData = BaplieParser.parse(reader);

            // Convert the BaplieData object to a JSON string
            ObjectMapper mapper = new ObjectMapper();
            String jsonResult = mapper.writeValueAsString(baplieData);
            return ResponseEntity.ok(jsonResult);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error reading EDI file: " + e.getMessage());
        }
    }

    @PostMapping("/codeco")
    public ResponseEntity<String> parseCodeco(@RequestParam("file") MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CodecoParser.CodecoData codecoData = CodecoParser.parse(reader);

            // Convert the CodecoData object to a JSON string
            ObjectMapper mapper = new ObjectMapper();
            String jsonResult = mapper.writeValueAsString(codecoData);
            return ResponseEntity.ok(jsonResult);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error reading EDI file: " + e.getMessage());
        }
    }
}
