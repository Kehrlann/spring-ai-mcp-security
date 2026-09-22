---
theme: default
class: 'text-center'
highlighter: prism
lineNumbers: true
transition: none
# use UnoCSS
css: unocss
aspectRatio: "16/9"
colorSchema: "light"
canvasWidth: 1024
---

# **Authz and Security for MCP**

<br>

### Daniel Garnier-Moiroux

OWASP AppSecDays France, 2026-09-24


---
layout: image-right
image: /daniel-intro-small.jpg
hideInToc: true
class: smaller
---

#### Daniel

### Garnier-Moiroux

<br>

Software Engineer

- <logos-spring-icon /> Spring
- <logos-model-context-protocol-icon /> MCP / java-sdk
- <logos-bluesky /> @garnier.wf
- <logos-firefox /> https://garnier.wf/
- <logos-github-icon /> github.com/Kehrlann/
- <fluent-emoji-flat-envelope-with-arrow /> contact@garnier.wf

---

## Authz and Security for MCP

<br>

1. MCP tl;dr
1. STDIO security profile
1. HTTP authorization
1. What's next?

---

## Authz and Security for MCP

<br>

1. **MCP tl;dr**
1. STDIO security profile
1. HTTP authorization
1. What's next?

---

## MCP: Why?

<br>

LLMs create text (or bytes) from their training set.

They lack:

1. Up-to-date information
1. Ability to perform "actions"

Can't perform an "Observe -> Decide -> Act" loop

---

## Enter MCP

<br>

Model Context Protocol

https://modelcontextprotocol.io

By Anthropic, now AAIF

---
layout: image
image: /mcp-flow-cc-1.png
---

---
layout: image
image: /mcp-flow-cc-2.png
---

---
layout: image
image: /mcp-flow-cc-3.png
---

---
layout: image
image: /mcp-flow-cc-4.png
---

---
layout: image
image: /mcp-flow-cc-5.png
---

---
layout: image
image: /mcp-flow-cc-6.png
---

---
layout: image
image: /mcp-flow-cc-7.png
---

---
layout: image
image: /mcp-flow-cc-8.png
---

---
layout: cover
---

# How it runs

---
layout: image
image: mcp-architecture-clarification.png
class: background-contain
---

---
layout: image
image: mcp-stdio-vs-http-1.png
class: background-contain
---

---
layout: image
image: mcp-stdio-vs-http-2.png
class: background-contain
---

---
layout: image
image: mcp-stdio-vs-http-3.png
class: background-contain
---

---

## Authz and Security for MCP

<br>

1. MCP tl;dr
1. **STDIO security profile**
1. HTTP authorization
1. What's next?

---

# A word on STDIO

&nbsp;

<v-clicks>

- Google "XYZ MCP", community-driven https://registry.modelcontextprotocol.io
- `npx run ...`, no integrity checks
- `-e API_KEY=...`, no standard auth
- Full permissions, "it's not a sandbox"

</v-clicks>

<v-click>

## 🤘 **STD-YOLO**

</v-click>


---

## Authz and Security for MCP

<br>

1. MCP tl;dr
1. STDIO security profile
1. **HTTP authorization**
1. What's next?

---

## Authorization in MCP-over-HTTP

<br>

[MCP Specification](https://modelcontextprotocol.io/specification/2026-07-28/basic/authorization)

Based on OAuth2: the MCP client sends a token to the MCP server.

---

## Challenges

<br>

OAuth2 requires a pre-existing relationship between Client, Resource Server and Auth Server.

That's not what we want to do with MCP: _users_ point their AI app at any server they like.

---
layout: image
image: /oauth-1.png
---

---
layout: image
image: /oauth-2.png
---

---
layout: image
image: /oauth-3.png
---

---
layout: cover
---

## **⚠️ In the case of MCP**

---
layout: image
image: /oauth-4.png
---

---
layout: image
image: /oauth-5.png
---

---

## Problem: discovering the auth server

<br>

Discovery via MCP server metadata

- **RFC 9728** OAuth 2.0 Protected Resource Metadata
    - MCP Server: `/.well-known/oauth-protected-resource`
- **RFC 8414** OAuth 2.0 Authorization Server Metadata
    - Authz Server: `/.well-known/oauth-authorization-server`

Example: https://mcp.figma.com/mcp

---

## Problem: pre-registration

<br>

No pre-registration needed, either:

- **RFC 7591** OAuth 2.0 Dynamic Client Registration Protocol
    - Authz Server exposes an open `registration_endpoint`
- **Draft** OAuth Client ID Metadata Document
    - Client sends `client_id=https://client.example.com/client`

---
layout: two-cols-header
class: smaller
---

## Dynamic Client Registration (DCR)

[**RFC 7591** OAuth 2.0 Dynamic Client Registration Protocol](https://datatracker.ietf.org/doc/html/rfc7591)


::left::

<v-click>

```
POST /<registration_endpoint>
{
  "redirect_uris": [...],
  "grant_types": [...],
  "scope": "read write",
  "client_name": "MCP Ex",
  ...
}
```

</v-click>

::right::

<v-click>

```
200 OK
{
  "client_id": ...,
  "client_secret": ...,
  ...
}
```

</v-click>


<style>
.two-cols-header {
    grid-template-rows: auto 1fr;
    column-gap: 50px;
    row-gap: 20px;
}
pre {
    background-color: #eee;
    font-size: .9rem;
    padding: 10px;
}
</style>

---

## Dynamic Client Registration (DCR)

[**RFC 7591** OAuth 2.0 Dynamic Client Registration Protocol](https://datatracker.ietf.org/doc/html/rfc7591)

<br>

<v-clicks>

- Open registration: DoS?
- Registration policies?
- Lifecycle management?

</v-clicks>

---

## Client ID Metadata Document

[**Draft** Client ID Metadata Document (CIMD)](https://datatracker.ietf.org/doc/draft-ietf-oauth-client-id-metadata-document/)

<br>

<v-click>

- Client exposes a metadata document
    - https://client.example.com/cimd.json
- Uses the URI as its client ID: no pre-registration!
- Auth server fetches document and validates

</v-click>

---

## Client ID Metadata Document

[**Draft** Client ID Metadata Document (CIMD)](https://datatracker.ietf.org/doc/draft-ietf-oauth-client-id-metadata-document/)

<br>

<v-clicks depth="2">

- Client policies?
- SSRF
    - No localhost support
- Lack of support from major players
    - e.g. AWS Cognito, Microsoft EntraID, ...
    - Keycloak: it's a work-in-progress ([#47765](https://github.com/keycloak/keycloak/issues/47765))


</v-clicks>

---

## Authz and Security for MCP

<br>

1. MCP tl;dr
1. STDIO security profile
1. HTTP authorization
1. **What's next?**

---

## Evolution across spec versions

<br>

<v-clicks at="0">

- V1 (2025-03)
    - MCP Server == auth server (issues tokens)
- V2 (2025-06)
    - Actually no, separate auth server
    - "Dynamic Client Registration" (DCR)
- V3 (2025-11)
    - Actually no, don't do DCR
    - "Client ID Metadata Document" (CIMD)

</v-clicks>

---

## Evolution across spec versions

<br>

<v-clicks depth="2">

- V4 (2026-07)
    - No major change 🎉
    - Security Best Practices
        - Refresh token guidance
        - Mix-Up attacks
        - ...
    - "Enterprise auth" extension
        - [Cross-App Access (XAA)](https://xaa.dev/)

</v-clicks>

---

## Where do we go from here?

&nbsp;

- STDIO wire format is being reworked
    - Might be some shape or form of HTTP
- CIMD is here to stay
    - But not quite there yet
- Authz gateways are the current workaround

---

## References

&nbsp;

#### **<logos-github-icon /> https://github.com/Kehrlann/spring-ai-mcp-security**

<div style="float:right; margin-right: 50px; text-align: center;">
    <img src="/qr-code-owasp.png" style="margin-bottom: -45px; height: 300px;" >
</div>

<br>

- <logos-bluesky /> @garnier.wf
- <logos-firefox /> https://garnier.wf/
- <fluent-emoji-flat-envelope-with-arrow /> contact@garnier.wf

---
layout: image
hideInToc: true
image: /meet-me.jpg
class: end
---

# **Merci 😊**

