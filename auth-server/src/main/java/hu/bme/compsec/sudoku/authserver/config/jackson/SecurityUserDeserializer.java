package hu.bme.compsec.sudoku.authserver.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import hu.bme.compsec.sudoku.authserver.config.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class SecurityUserDeserializer extends JsonDeserializer<SecurityUser> {


    @Override
    public SecurityUser deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        final JsonNode authoritiesNode = readJsonNode(jsonNode, "authorities");
        Set<GrantedAuthority> authorities = getUserAuthorities(mapper, authoritiesNode);

        Long id = readJsonNode(jsonNode, "id").asLong(); //TODO: UUID
        String username = readJsonNode(jsonNode, "username").asText();
        JsonNode passwordNode = readJsonNode(jsonNode, "password");
        String password = passwordNode.asText("");
        boolean enabled = readJsonNode(jsonNode, "enabled").asBoolean();


        return new SecurityUser(id, username, password, authorities, enabled);

    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }

    private Set<GrantedAuthority> getUserAuthorities(final ObjectMapper mapper, final JsonNode authoritiesNode) throws IOException {
        Set<GrantedAuthority> userAuthorities = new HashSet<>();
        if (authoritiesNode != null && authoritiesNode.isArray()) {
                for (final JsonNode objNode : authoritiesNode) {
                    if (objNode.isArray()) {
                        ArrayNode arrayNode = (ArrayNode) objNode;
                        for (JsonNode elementNode : arrayNode) {
                            SimpleGrantedAuthority userAuthority = mapper.readValue(elementNode.traverse(mapper), SimpleGrantedAuthority.class);
                            log.debug(userAuthority.toString());
                            userAuthorities.add(userAuthority);
                        }
                    }
                }
        }
        return userAuthorities;
    }

}
