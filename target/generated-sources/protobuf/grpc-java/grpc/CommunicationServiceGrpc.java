package grpc;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.7.0)",
    comments = "Source: CommunicationService.proto")
public final class CommunicationServiceGrpc {

  private CommunicationServiceGrpc() {}

  public static final String SERVICE_NAME = "grpc.CommunicationService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<grpc.CommunicationServiceOuterClass.HelloRequest,
      grpc.CommunicationServiceOuterClass.HelloResponse> METHOD_CONTACT =
      io.grpc.MethodDescriptor.<grpc.CommunicationServiceOuterClass.HelloRequest, grpc.CommunicationServiceOuterClass.HelloResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "grpc.CommunicationService", "contact"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              grpc.CommunicationServiceOuterClass.HelloRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              grpc.CommunicationServiceOuterClass.HelloResponse.getDefaultInstance()))
          .setSchemaDescriptor(new CommunicationServiceMethodDescriptorSupplier("contact"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<grpc.CommunicationServiceOuterClass.Exit,
      grpc.CommunicationServiceOuterClass.HelloResponse> METHOD_EXIT =
      io.grpc.MethodDescriptor.<grpc.CommunicationServiceOuterClass.Exit, grpc.CommunicationServiceOuterClass.HelloResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "grpc.CommunicationService", "exit"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              grpc.CommunicationServiceOuterClass.Exit.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              grpc.CommunicationServiceOuterClass.HelloResponse.getDefaultInstance()))
          .setSchemaDescriptor(new CommunicationServiceMethodDescriptorSupplier("exit"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<grpc.CommunicationServiceOuterClass.Election,
      grpc.CommunicationServiceOuterClass.Elected> METHOD_ELECTION =
      io.grpc.MethodDescriptor.<grpc.CommunicationServiceOuterClass.Election, grpc.CommunicationServiceOuterClass.Elected>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "grpc.CommunicationService", "election"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              grpc.CommunicationServiceOuterClass.Election.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              grpc.CommunicationServiceOuterClass.Elected.getDefaultInstance()))
          .setSchemaDescriptor(new CommunicationServiceMethodDescriptorSupplier("election"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<grpc.CommunicationServiceOuterClass.Measurement,
      grpc.CommunicationServiceOuterClass.LocalStatsResponse> METHOD_LOCAL_STAT =
      io.grpc.MethodDescriptor.<grpc.CommunicationServiceOuterClass.Measurement, grpc.CommunicationServiceOuterClass.LocalStatsResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "grpc.CommunicationService", "localStat"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              grpc.CommunicationServiceOuterClass.Measurement.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              grpc.CommunicationServiceOuterClass.LocalStatsResponse.getDefaultInstance()))
          .setSchemaDescriptor(new CommunicationServiceMethodDescriptorSupplier("localStat"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<grpc.CommunicationServiceOuterClass.BoostRequest,
      grpc.CommunicationServiceOuterClass.BoostResponse> METHOD_BOOST =
      io.grpc.MethodDescriptor.<grpc.CommunicationServiceOuterClass.BoostRequest, grpc.CommunicationServiceOuterClass.BoostResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "grpc.CommunicationService", "boost"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              grpc.CommunicationServiceOuterClass.BoostRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              grpc.CommunicationServiceOuterClass.BoostResponse.getDefaultInstance()))
          .setSchemaDescriptor(new CommunicationServiceMethodDescriptorSupplier("boost"))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CommunicationServiceStub newStub(io.grpc.Channel channel) {
    return new CommunicationServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CommunicationServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new CommunicationServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CommunicationServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new CommunicationServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class CommunicationServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * registrazione delle case
     * </pre>
     */
    public void contact(grpc.CommunicationServiceOuterClass.HelloRequest request,
        io.grpc.stub.StreamObserver<grpc.CommunicationServiceOuterClass.HelloResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CONTACT, responseObserver);
    }

    /**
     * <pre>
     * uscita delle case
     * </pre>
     */
    public void exit(grpc.CommunicationServiceOuterClass.Exit request,
        io.grpc.stub.StreamObserver<grpc.CommunicationServiceOuterClass.HelloResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_EXIT, responseObserver);
    }

    /**
     * <pre>
     * elezione di un coordinatore
     * </pre>
     */
    public void election(grpc.CommunicationServiceOuterClass.Election request,
        io.grpc.stub.StreamObserver<grpc.CommunicationServiceOuterClass.Elected> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ELECTION, responseObserver);
    }

    /**
     * <pre>
     * invio delle statistiche locali
     * </pre>
     */
    public void localStat(grpc.CommunicationServiceOuterClass.Measurement request,
        io.grpc.stub.StreamObserver<grpc.CommunicationServiceOuterClass.LocalStatsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LOCAL_STAT, responseObserver);
    }

    /**
     * <pre>
     * richiesta di consumo extra
     * </pre>
     */
    public void boost(grpc.CommunicationServiceOuterClass.BoostRequest request,
        io.grpc.stub.StreamObserver<grpc.CommunicationServiceOuterClass.BoostResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_BOOST, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_CONTACT,
            asyncUnaryCall(
              new MethodHandlers<
                grpc.CommunicationServiceOuterClass.HelloRequest,
                grpc.CommunicationServiceOuterClass.HelloResponse>(
                  this, METHODID_CONTACT)))
          .addMethod(
            METHOD_EXIT,
            asyncUnaryCall(
              new MethodHandlers<
                grpc.CommunicationServiceOuterClass.Exit,
                grpc.CommunicationServiceOuterClass.HelloResponse>(
                  this, METHODID_EXIT)))
          .addMethod(
            METHOD_ELECTION,
            asyncUnaryCall(
              new MethodHandlers<
                grpc.CommunicationServiceOuterClass.Election,
                grpc.CommunicationServiceOuterClass.Elected>(
                  this, METHODID_ELECTION)))
          .addMethod(
            METHOD_LOCAL_STAT,
            asyncUnaryCall(
              new MethodHandlers<
                grpc.CommunicationServiceOuterClass.Measurement,
                grpc.CommunicationServiceOuterClass.LocalStatsResponse>(
                  this, METHODID_LOCAL_STAT)))
          .addMethod(
            METHOD_BOOST,
            asyncUnaryCall(
              new MethodHandlers<
                grpc.CommunicationServiceOuterClass.BoostRequest,
                grpc.CommunicationServiceOuterClass.BoostResponse>(
                  this, METHODID_BOOST)))
          .build();
    }
  }

  /**
   */
  public static final class CommunicationServiceStub extends io.grpc.stub.AbstractStub<CommunicationServiceStub> {
    private CommunicationServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CommunicationServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CommunicationServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CommunicationServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * registrazione delle case
     * </pre>
     */
    public void contact(grpc.CommunicationServiceOuterClass.HelloRequest request,
        io.grpc.stub.StreamObserver<grpc.CommunicationServiceOuterClass.HelloResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CONTACT, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * uscita delle case
     * </pre>
     */
    public void exit(grpc.CommunicationServiceOuterClass.Exit request,
        io.grpc.stub.StreamObserver<grpc.CommunicationServiceOuterClass.HelloResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_EXIT, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * elezione di un coordinatore
     * </pre>
     */
    public void election(grpc.CommunicationServiceOuterClass.Election request,
        io.grpc.stub.StreamObserver<grpc.CommunicationServiceOuterClass.Elected> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ELECTION, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * invio delle statistiche locali
     * </pre>
     */
    public void localStat(grpc.CommunicationServiceOuterClass.Measurement request,
        io.grpc.stub.StreamObserver<grpc.CommunicationServiceOuterClass.LocalStatsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_LOCAL_STAT, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * richiesta di consumo extra
     * </pre>
     */
    public void boost(grpc.CommunicationServiceOuterClass.BoostRequest request,
        io.grpc.stub.StreamObserver<grpc.CommunicationServiceOuterClass.BoostResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_BOOST, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class CommunicationServiceBlockingStub extends io.grpc.stub.AbstractStub<CommunicationServiceBlockingStub> {
    private CommunicationServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CommunicationServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CommunicationServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CommunicationServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * registrazione delle case
     * </pre>
     */
    public grpc.CommunicationServiceOuterClass.HelloResponse contact(grpc.CommunicationServiceOuterClass.HelloRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CONTACT, getCallOptions(), request);
    }

    /**
     * <pre>
     * uscita delle case
     * </pre>
     */
    public grpc.CommunicationServiceOuterClass.HelloResponse exit(grpc.CommunicationServiceOuterClass.Exit request) {
      return blockingUnaryCall(
          getChannel(), METHOD_EXIT, getCallOptions(), request);
    }

    /**
     * <pre>
     * elezione di un coordinatore
     * </pre>
     */
    public grpc.CommunicationServiceOuterClass.Elected election(grpc.CommunicationServiceOuterClass.Election request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ELECTION, getCallOptions(), request);
    }

    /**
     * <pre>
     * invio delle statistiche locali
     * </pre>
     */
    public grpc.CommunicationServiceOuterClass.LocalStatsResponse localStat(grpc.CommunicationServiceOuterClass.Measurement request) {
      return blockingUnaryCall(
          getChannel(), METHOD_LOCAL_STAT, getCallOptions(), request);
    }

    /**
     * <pre>
     * richiesta di consumo extra
     * </pre>
     */
    public grpc.CommunicationServiceOuterClass.BoostResponse boost(grpc.CommunicationServiceOuterClass.BoostRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_BOOST, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class CommunicationServiceFutureStub extends io.grpc.stub.AbstractStub<CommunicationServiceFutureStub> {
    private CommunicationServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CommunicationServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CommunicationServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CommunicationServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * registrazione delle case
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.CommunicationServiceOuterClass.HelloResponse> contact(
        grpc.CommunicationServiceOuterClass.HelloRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CONTACT, getCallOptions()), request);
    }

    /**
     * <pre>
     * uscita delle case
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.CommunicationServiceOuterClass.HelloResponse> exit(
        grpc.CommunicationServiceOuterClass.Exit request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_EXIT, getCallOptions()), request);
    }

    /**
     * <pre>
     * elezione di un coordinatore
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.CommunicationServiceOuterClass.Elected> election(
        grpc.CommunicationServiceOuterClass.Election request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ELECTION, getCallOptions()), request);
    }

    /**
     * <pre>
     * invio delle statistiche locali
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.CommunicationServiceOuterClass.LocalStatsResponse> localStat(
        grpc.CommunicationServiceOuterClass.Measurement request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_LOCAL_STAT, getCallOptions()), request);
    }

    /**
     * <pre>
     * richiesta di consumo extra
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.CommunicationServiceOuterClass.BoostResponse> boost(
        grpc.CommunicationServiceOuterClass.BoostRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_BOOST, getCallOptions()), request);
    }
  }

  private static final int METHODID_CONTACT = 0;
  private static final int METHODID_EXIT = 1;
  private static final int METHODID_ELECTION = 2;
  private static final int METHODID_LOCAL_STAT = 3;
  private static final int METHODID_BOOST = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CommunicationServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(CommunicationServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CONTACT:
          serviceImpl.contact((grpc.CommunicationServiceOuterClass.HelloRequest) request,
              (io.grpc.stub.StreamObserver<grpc.CommunicationServiceOuterClass.HelloResponse>) responseObserver);
          break;
        case METHODID_EXIT:
          serviceImpl.exit((grpc.CommunicationServiceOuterClass.Exit) request,
              (io.grpc.stub.StreamObserver<grpc.CommunicationServiceOuterClass.HelloResponse>) responseObserver);
          break;
        case METHODID_ELECTION:
          serviceImpl.election((grpc.CommunicationServiceOuterClass.Election) request,
              (io.grpc.stub.StreamObserver<grpc.CommunicationServiceOuterClass.Elected>) responseObserver);
          break;
        case METHODID_LOCAL_STAT:
          serviceImpl.localStat((grpc.CommunicationServiceOuterClass.Measurement) request,
              (io.grpc.stub.StreamObserver<grpc.CommunicationServiceOuterClass.LocalStatsResponse>) responseObserver);
          break;
        case METHODID_BOOST:
          serviceImpl.boost((grpc.CommunicationServiceOuterClass.BoostRequest) request,
              (io.grpc.stub.StreamObserver<grpc.CommunicationServiceOuterClass.BoostResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class CommunicationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CommunicationServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpc.CommunicationServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CommunicationService");
    }
  }

  private static final class CommunicationServiceFileDescriptorSupplier
      extends CommunicationServiceBaseDescriptorSupplier {
    CommunicationServiceFileDescriptorSupplier() {}
  }

  private static final class CommunicationServiceMethodDescriptorSupplier
      extends CommunicationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    CommunicationServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CommunicationServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CommunicationServiceFileDescriptorSupplier())
              .addMethod(METHOD_CONTACT)
              .addMethod(METHOD_EXIT)
              .addMethod(METHOD_ELECTION)
              .addMethod(METHOD_LOCAL_STAT)
              .addMethod(METHOD_BOOST)
              .build();
        }
      }
    }
    return result;
  }
}
